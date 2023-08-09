package com.exchangeBook.ExchangeBook.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.exchangeBook.ExchangeBook.dto.AddressDto;
import com.exchangeBook.ExchangeBook.entity.Address;
import com.exchangeBook.ExchangeBook.entity.EPostStatus;
import com.exchangeBook.ExchangeBook.entity.ERole;
import com.exchangeBook.ExchangeBook.entity.EUserStatus;
import com.exchangeBook.ExchangeBook.entity.Image;
import com.exchangeBook.ExchangeBook.entity.Post;
import com.exchangeBook.ExchangeBook.entity.User;
import com.exchangeBook.ExchangeBook.mapper.PostMapper;
import com.exchangeBook.ExchangeBook.mapper.UserMapper;
import com.exchangeBook.ExchangeBook.payload.request.UserRequest;
import com.exchangeBook.ExchangeBook.payload.response.MessageResponse;
import com.exchangeBook.ExchangeBook.payload.response.PostPagingResponse;
import com.exchangeBook.ExchangeBook.payload.response.PostResponse;
import com.exchangeBook.ExchangeBook.payload.response.UserDetailResponse;
import com.exchangeBook.ExchangeBook.payload.response.UserPagingResponse;
import com.exchangeBook.ExchangeBook.payload.response.UserResponse;
import com.exchangeBook.ExchangeBook.repository.AddressRepository;
import com.exchangeBook.ExchangeBook.repository.ImageRepository;
import com.exchangeBook.ExchangeBook.repository.PostRepository;
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
	PostRepository postRepository;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	PostMapper postMapper;

	private final String DEFAULT_AVATAR_NAME = "default_user_avatar.png";

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public ResponseEntity<?> getAllUsers(Integer page, Integer size) {

		Pageable paging = PageRequest.of(page - 1, size);
		Page<User> paged = userRepository.findAll(paging);
		UserPagingResponse userPagingResponse = null;

		try {
			List<UserResponse> usersResponses = paged.getContent().stream().map(user -> userMapper.toUserResponse(user))
					.collect(Collectors.toList());

			userPagingResponse = UserPagingResponse.builder().totalItems(paged.getTotalElements())
					.totalPages(paged.getTotalPages()).usersResponses(usersResponses).build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new MessageResponse("Error: Get all users failed!"));
		}

		return ResponseEntity.ok().body(userPagingResponse);
	}

	@Override
	public ResponseEntity<?> getOneUser(Long id) {
		UserDetailResponse userDetailResponse = null;
		try {
			User user = userRepository.findById(id).get();
			userDetailResponse = userMapper.toUserDetailResponse(user);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new MessageResponse("Get user failed!"));
		}
		return ResponseEntity.ok().body(userDetailResponse);
	}

	@Override
	public ResponseEntity<?> updateOneUser(Long id, ERole role, EUserStatus status) {
		try {
			User user = userRepository.findById(id).get();
			if (role != null) {
				user.setRole(role);
			}
			if (status != null) {
				user.setStatus(status);
			}
			user = userRepository.save(user);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new MessageResponse("Update user failed!"));
		}
		return ResponseEntity.ok().body(new MessageResponse("Update user successfully!"));
	}

	@Override
	public ResponseEntity<?> updateCurrentUser(UserRequest userRequest) {
		User user = null;

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal.toString() == "anonymousUser") {
			return ResponseEntity.badRequest().body(new MessageResponse("Unauthorized"));
		}

		UserDetailsImpl userDetail = (UserDetailsImpl) principal;
		user = userRepository.findById(userDetail.getId()).get();

		try {
			Image image = imageRepository.findById(user.getAvatar().getId()).get();
			if (!image.getName().equals(DEFAULT_AVATAR_NAME)) {
				imageService.deleteImage(image.getId());
			}

			if (user.getAddress() != null) {
				addressRepository.deleteById(user.getAddress().getId());
			}

			Image avatar = null;
			if (userRequest.getAvatar() != null) {
				avatar = imageService.uploadImage(userRequest.getAvatar());
				user.setAvatar(avatar);
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

			user = userRepository.save(user);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new MessageResponse("Update current user failed!"));

		}
		return ResponseEntity.ok().body(new MessageResponse("Update current user successfully!"));
	}

	@Override
	public ResponseEntity<?> deleteOneUser(Long id) {
		try {
			User user = userRepository.findById(id).get();
			user.setStatus(EUserStatus.DELETED);
			user = userRepository.save(user);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("User id not found!"));
		}
		return ResponseEntity.ok().body(new MessageResponse("Delete user successfully!"));
	}

	@Override
	public ResponseEntity<?> getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal.toString() == "anonymousUser") {
			return ResponseEntity.badRequest().body(new MessageResponse("Unauthorized"));
		}
		UserDetailsImpl userDetail = (UserDetailsImpl) principal;
		User user = userRepository.findById(userDetail.getId()).get();
		UserDetailResponse userDetailResponse = userMapper.toUserDetailResponse(user);
		return ResponseEntity.ok().body(userDetailResponse);
	}

	@Override
	public ResponseEntity<?> getCurrentUserPosts(Integer page, Integer size, String sortBy, EPostStatus status) {
		User user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal.toString() == "anonymousUser") {
			return ResponseEntity.badRequest().body(new MessageResponse("Unauthorized"));
		}
		UserDetailsImpl userDetail = (UserDetailsImpl) principal;
		user = userRepository.findById(userDetail.getId()).get();
		PostPagingResponse postPagingResponse = null;
		try {
			Pageable paging = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, sortBy));
			Page<Post> paged = postRepository.findAllByUserIdAndStatusLike(user.getId(), status, paging);

			List<PostResponse> postsResponses = paged.stream().map(post -> postMapper.toPostsResponse(post))
					.collect(Collectors.toList());
			postPagingResponse = new PostPagingResponse();
			postPagingResponse.setTotalItems(paged.getTotalElements());
			postPagingResponse.setTotalPages(paged.getTotalPages());
			postPagingResponse.setPostsResponses(postsResponses);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new MessageResponse("Get current user's posts failed!"));
		}
		return ResponseEntity.ok().body(postPagingResponse);
	}

	@Override
	public ResponseEntity<?> getOneUserPosts(Long id, Integer page, Integer size, String sortBy) {
		User user = userRepository.findById(id).get();
		PostPagingResponse postPagingResponse = null;
		try {
			Pageable paging = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, sortBy));
			Page<Post> paged = postRepository.findAllByUserId(user.getId(), paging);

			List<PostResponse> postsResponses = paged.stream().map(post -> postMapper.toPostsResponse(post))
					.collect(Collectors.toList());

			postPagingResponse = new PostPagingResponse();
			postPagingResponse.setTotalItems(paged.getTotalElements());
			postPagingResponse.setTotalPages(paged.getTotalPages());
			postPagingResponse.setPostsResponses(postsResponses);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new MessageResponse("Get user's posts failed"));
		}
		return ResponseEntity.ok().body(postPagingResponse);
	}

	@Override
	public boolean checkPoint() {
//		User user = userRepository.findById(userId).get();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userEmail = "";
		if (principal.toString() != "anonymousUser") {
			userEmail = ((UserDetailsImpl) principal).getEmail();
		}
		User user = userRepository.findByEmail(userEmail).get();
		if (user.getPoint() > 0)
			return true;
		return false;
	}

	@Override
	public int returnPoint() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userEmail = "";
		if (principal.toString() != "anonymousUser") {
			userEmail = ((UserDetailsImpl) principal).getEmail();
		}
		User user = userRepository.findByEmail(userEmail).get();
		return user.getPoint();
	}

	@Override
	public boolean acceptThePost(Long idPost) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userEmail = "";
		if (principal.toString() != "anonymousUser") {
			userEmail = ((UserDetailsImpl) principal).getEmail();
		}
		User user = userRepository.findByEmail(userEmail).get();
		User postOwner;
		if (user.getRole() == ERole.ROLE_ADMIN) {
			Post post = postRepository.findById(idPost).get();
			post.setStatus(EPostStatus.APPROVED);
			postRepository.save(post);
			postOwner = post.getUser();
			postOwner.setPoint(postOwner.getPoint() + 1);
			userRepository.save(postOwner);
			return true;
		}
		return false;
	}
}