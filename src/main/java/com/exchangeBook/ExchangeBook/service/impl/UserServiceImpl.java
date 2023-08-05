package com.exchangeBook.ExchangeBook.service.impl;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.exchangeBook.ExchangeBook.dto.AddressDto;
import com.exchangeBook.ExchangeBook.entity.Address;
import com.exchangeBook.ExchangeBook.entity.ERole;
import com.exchangeBook.ExchangeBook.entity.EUserStatus;
import com.exchangeBook.ExchangeBook.entity.Image;
import com.exchangeBook.ExchangeBook.entity.User;
import com.exchangeBook.ExchangeBook.mapper.UserMapper;
import com.exchangeBook.ExchangeBook.payload.request.UserRequest;
import com.exchangeBook.ExchangeBook.payload.response.UserDetailResponse;
import com.exchangeBook.ExchangeBook.payload.response.UserPagingResponse;
import com.exchangeBook.ExchangeBook.payload.response.UserResponse;
import com.exchangeBook.ExchangeBook.property.FileStorageProperties;
import com.exchangeBook.ExchangeBook.repository.AddressRepository;
import com.exchangeBook.ExchangeBook.repository.ImageRepository;
import com.exchangeBook.ExchangeBook.repository.UserRepository;
import com.exchangeBook.ExchangeBook.security.jwt.JwtUtils;
import com.exchangeBook.ExchangeBook.security.service.UserDetailsImpl;
import com.exchangeBook.ExchangeBook.service.ImageService;
import com.exchangeBook.ExchangeBook.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserMapper userMapper;

	@Autowired
	ImageService imageService;

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	ImageRepository imageRepository;

	@Autowired
	JwtUtils jwtUtils;

//	@Autowired
//	FileStorageProperties properties;
//
//	private final Path rootLocation = Paths.get(properties.getUploadDir());

	private final Path rootLocation;

	public UserServiceImpl(FileStorageProperties properties) {
		this.rootLocation = Paths.get(properties.getUploadDir());
	}

	private final String DEFAULT_AVATAR_NAME = "default_user_avatar.png";

	@Override
	public UserPagingResponse getAllUsers(Integer page, Integer size) {

		Pageable paging = PageRequest.of(page - 1, size);
		Page<User> paged = userRepository.findAll(paging);

		List<UserResponse> usersResponses = paged.getContent().stream().map(user -> userMapper.toUserResponse(user))
				.collect(Collectors.toList());

		UserPagingResponse userPagingResponse = UserPagingResponse.builder().totalItems(paged.getTotalElements())
				.totalPages(paged.getTotalPages()).usersResponses(usersResponses).build();

		return userPagingResponse;
	}

	@Override
	public UserDetailResponse getOneUser(Long id) {
		User user = userRepository.findById(id).get();
		return userMapper.toUserDetailResponse(user);
	}

	@Override
	public UserResponse updateOneUser(Long id, ERole role, EUserStatus status) {
		User user = userRepository.findById(id).get();
		if(role != null) {
			user.setRole(role);
		}
		if(status != null) {
			user.setStatus(status);
		}
		
		user = userRepository.save(user);

		UserResponse userResponse = userMapper.toUserResponse(user);

		return userResponse;
	}
	
	@Override
	public UserResponse updateCurrentUser(UserRequest userRequest) {
		User user = null;
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal.toString() == "anonymousUser") {
			return null;
		}
		
		UserDetailsImpl userDetail = (UserDetailsImpl) principal;
		user = userRepository.findById(userDetail.getId()).get();
		
		Image image = imageRepository.findById(user.getAvatar().getId()).get();
		imageService.deleteImage(image.getName());
		
		addressRepository.deleteById(user.getAddress().getId());
		
		Image avatar = null;
		if (userRequest.getAvatar() != null) {
			avatar = imageService.uploadImage(userRequest.getAvatar());
		} else {
			String fileName = DEFAULT_AVATAR_NAME;
			String contentType = fileName.split("[.]")[1];
			Path filePath = rootLocation.resolve(Paths.get(fileName)).normalize().toAbsolutePath();
			File file = new File(filePath.toString());
			avatar = imageRepository.save(Image.builder().name(fileName).contentType(contentType).size(file.length())
					.path(filePath.toString()).build());
		}
		AddressDto addressDto = userRequest.getAddress();

		Address address = Address.builder().province(addressDto.getProvince()).district(addressDto.getDistrict())
				.ward(addressDto.getWard()).detail(addressDto.getDetail()).build();
		addressRepository.save(address);

		user.setFirstName(userRequest.getFirstName());
		user.setLastName(userRequest.getLastName());
		user.setPhoneNumber(userRequest.getPhoneNumber());
		user.setRole(userRequest.getRole() != null ? userRequest.getRole() : ERole.ROLE_USER);
		user.setAddress(address);
		user.setAvatar(avatar);

		user = userRepository.save(user);

		UserResponse userResponse = userMapper.toUserResponse(user);

		return userResponse;
	}

	@Override
	public UserResponse deleteOneUser(Long id) {
		User user = userRepository.findById(id).get();
		user.setStatus(EUserStatus.DELETED);
		user = userRepository.save(user);

		UserResponse userResponse = userMapper.toUserResponse(user);
		return userResponse;
	}

	@Override
	public UserDetailResponse getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal.toString() != "anonymousUser") {
			UserDetailsImpl userDetail = (UserDetailsImpl) principal;
			User user = userRepository.findById(userDetail.getId()).get();
			return userMapper.toUserDetailResponse(user);
		}
		return null;
	}

}
