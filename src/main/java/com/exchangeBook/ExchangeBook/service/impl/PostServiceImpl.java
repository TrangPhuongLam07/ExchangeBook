package com.exchangeBook.ExchangeBook.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.exchangeBook.ExchangeBook.entity.Category;
import com.exchangeBook.ExchangeBook.entity.EPostStatus;
import com.exchangeBook.ExchangeBook.entity.Image;
import com.exchangeBook.ExchangeBook.entity.Post;
import com.exchangeBook.ExchangeBook.entity.User;
import com.exchangeBook.ExchangeBook.mapper.PostMapper;
import com.exchangeBook.ExchangeBook.payload.request.PostRequest;
import com.exchangeBook.ExchangeBook.payload.response.MessageResponse;
import com.exchangeBook.ExchangeBook.payload.response.PostDetailResponse;
import com.exchangeBook.ExchangeBook.payload.response.PostPagingResponse;
import com.exchangeBook.ExchangeBook.payload.response.PostResponse;
import com.exchangeBook.ExchangeBook.repository.CategoryRepository;
import com.exchangeBook.ExchangeBook.repository.PostRepository;
import com.exchangeBook.ExchangeBook.repository.UserRepository;
import com.exchangeBook.ExchangeBook.security.service.UserDetailsImpl;
import com.exchangeBook.ExchangeBook.service.ImageService;
import com.exchangeBook.ExchangeBook.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	PostRepository postRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ImageService imageService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PostMapper postMapper;

	private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

	@Override
	public ResponseEntity<?> createNewPost(PostRequest postRequest) {
		User user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal.toString() == "anonymousUser") {
			return ResponseEntity.badRequest().body(new MessageResponse("Unauthorized"));

		}

//		Category category = categoryRepository.findById(postRequest.getCategory()).get();

		UserDetailsImpl userDetail = (UserDetailsImpl) principal;
		user = userRepository.findById(userDetail.getId()).get();

		try {
			Category category = categoryRepository.findById(postRequest.getCategory()).get();

			List<Image> imageList = imageService.uploadMultiImage(postRequest.getBase64Images());

			String strMaxDatetime = "9999-12-31 23:59:59.999999";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
			LocalDateTime maxDateTime = LocalDateTime.parse(strMaxDatetime, formatter);

			Post post = new Post();
			post.setTitle(postRequest.getTitle());
			post.setAuthor(postRequest.getAuthor());
			post.setDescription(postRequest.getDescription());
			post.setStatus(EPostStatus.CREATE_PENDING);
			post.setDateCreated(LocalDateTime.now());
			post.setDateUpdated(LocalDateTime.now());
			post.setDatePosted(maxDateTime);
			post.setCategory(category);
			post.setImages(imageList);
			post.setUser(user);
			postRepository.save(post);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.internalServerError().body(new MessageResponse("Create new post failed!"));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Create new post successfully!"));
	}

	@Override
	public ResponseEntity<?> getAllPosts(Integer page, Integer size, String sortBy, Specification<Post> spec) {
		Pageable paging = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, sortBy));
		Page<Post> paged = postRepository.findAll(spec, paging);
		List<PostResponse> postsResponses = null;
		try {
			postsResponses = paged.stream().map(post -> postMapper.toPostsResponse(post)).collect(Collectors.toList());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new MessageResponse("Get all posts failed!"));
		}
		PostPagingResponse postPagingResponse = new PostPagingResponse();
		postPagingResponse.setTotalItems(paged.getTotalElements());
		postPagingResponse.setTotalPages(paged.getTotalPages());
		postPagingResponse.setPostsResponses(postsResponses);

		return ResponseEntity.ok().body(postPagingResponse);
	}

	@Override
	public ResponseEntity<?> getOnePost(Long id) {
		Post post = postRepository.findById(id).get();
		PostDetailResponse postResponse = null;
		try {
			postResponse = postMapper.toPostDetailResponse(post);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new MessageResponse("Get post failed!"));
		}

		return ResponseEntity.ok().body(postResponse);
	}

	@Override
	public ResponseEntity<?> updateOnePost(Long id, PostRequest postRequest) {
		Post post = postRepository.findById(id).get();

		User user = userRepository.findById(post.getUser().getId()).get();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (!((UserDetailsImpl) principal).getEmail().equals(user.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Unauthorized"));
		}

		try {
			List<Image> oldImages = post.getImages();
			oldImages.forEach(image -> imageService.deleteImage(image.getId()));

			Category category = categoryRepository.findById(postRequest.getCategory()).get();
			List<Image> imageList = imageService.uploadMultiImage(postRequest.getBase64Images());

			post.setTitle(postRequest.getTitle());
			post.setAuthor(postRequest.getAuthor());
			post.setDescription(postRequest.getDescription());
			post.setStatus(EPostStatus.UPDATE_PENDING);
			post.setDateUpdated(LocalDateTime.now());
			post.setCategory(category);
			post.setImages(imageList);
			postRepository.save(post);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new MessageResponse("Update post failed!"));
		}

		return ResponseEntity.ok().body(new MessageResponse("Update post successfully!"));
	}

	@Override
	public ResponseEntity<?> updateStatusPost(Long id, EPostStatus status) {
		Post post = postRepository.findById(id).get();
		if (status.equals(EPostStatus.APPROVED)) {
			User user = post.getUser();
			user.setPoint(user.getPoint() + 1);
			userRepository.save(user);
		}
		post.setStatus(status);
		postRepository.save(post);
		return ResponseEntity.ok().body(new MessageResponse("Update post successfully!"));
	}

	@Override
	public ResponseEntity<?> deleteOnePost(Long id) {
		Post post = postRepository.findById(id).get();

		User user = post.getUser();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (!((UserDetailsImpl) principal).getEmail().equals(user.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Unauthorized!"));
		}
		post.setStatus(EPostStatus.HIDDEN);
		postRepository.save(post);

		return ResponseEntity.ok().body(new MessageResponse("Delete post successfully!"));
	}
}
