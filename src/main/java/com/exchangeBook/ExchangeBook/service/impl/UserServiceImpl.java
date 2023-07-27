package com.exchangeBook.ExchangeBook.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.exchangeBook.ExchangeBook.dto.UserDto;
import com.exchangeBook.ExchangeBook.entity.Address;
import com.exchangeBook.ExchangeBook.entity.EUserStatus;
import com.exchangeBook.ExchangeBook.entity.Image;
import com.exchangeBook.ExchangeBook.entity.User;
import com.exchangeBook.ExchangeBook.mapper.UserMapper;
import com.exchangeBook.ExchangeBook.payload.request.UserRequest;
import com.exchangeBook.ExchangeBook.payload.response.UserDetailResponse;
import com.exchangeBook.ExchangeBook.payload.response.UserPagingResponse;
import com.exchangeBook.ExchangeBook.payload.response.UsersResponse;
import com.exchangeBook.ExchangeBook.repository.AddressRepository;
import com.exchangeBook.ExchangeBook.repository.UserRepository;
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

	@Override
	public UserPagingResponse getAllUsers(Integer page, Integer size) {

		Pageable paging = PageRequest.of(page - 1, size);
		Page<User> paged = userRepository.findAll(paging);

		List<UsersResponse> usersResponses = paged.getContent().stream().map(user -> userMapper.toUsersResponse(user))
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
	public UserDto updateOneUser(Long id, UserRequest userRequest, MultipartFile image) {

		Image avatar = imageService.uploadImage(image);
		Address address = addressRepository.findById(id).get();
		
		User user = userRepository.findById(id).get();
		user.setEmail(userRequest.getEmail());
		user.setFirstName(userRequest.getFirstName());
		user.setLastName(userRequest.getLastName());
		user.setPhoneNumber(userRequest.getPhoneNumber());
		user.setRole(userRequest.getRole());
		user.setAddress(address);
		user.setAvatar(avatar);
		
		user = userRepository.save(user);
		
		UserDto userDto = userMapper.toUserDto(user);
		
		return userDto;
	}

	@Override
	public UserDto deleteOneUser(Long id) {
		User user = userRepository.findById(id).get();
		user.setStatus(EUserStatus.DELETED);
		user = userRepository.save(user);
		
		UserDto userDto = userMapper.toUserDto(user);
		return userDto;
	}

}
