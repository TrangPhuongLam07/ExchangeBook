package com.exchangeBook.ExchangeBook.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.exchangeBook.ExchangeBook.dto.PostDto;
import com.exchangeBook.ExchangeBook.entity.Category;
import com.exchangeBook.ExchangeBook.entity.EPostStatus;
import com.exchangeBook.ExchangeBook.entity.Image;
import com.exchangeBook.ExchangeBook.entity.Post;
import com.exchangeBook.ExchangeBook.mapper.PostMapper;
import com.exchangeBook.ExchangeBook.payload.request.PostRequest;
import com.exchangeBook.ExchangeBook.payload.response.PostDetailResponse;
import com.exchangeBook.ExchangeBook.payload.response.PostPagingResponse;
import com.exchangeBook.ExchangeBook.payload.response.PostsResponse;
import com.exchangeBook.ExchangeBook.repository.CategoryRepository;
import com.exchangeBook.ExchangeBook.repository.PostRepository;
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
	PostMapper postMapper;

	@Override

	public PostDto createNewPost(PostRequest postRequest, MultipartFile[] images) {
		Category category = categoryRepository.findById(postRequest.getCategory()).get();

		List<Image> imageList = imageService.uploadMultiImage(images);

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
		PostDto createdPost = postMapper.toPostDto(postRepository.save(post));

		return createdPost;
	}

	@Override
	public PostPagingResponse getAllPosts(Integer page, Integer size, String sortBy, Specification<Post> spec) {
		Pageable paging = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, sortBy));
		Page<Post> paged = postRepository.findAll(spec, paging);

		List<PostsResponse> postsResponses = paged.stream().map(post -> postMapper.toPostsResponse(post))
				.collect(Collectors.toList());

		PostPagingResponse postPagingResponse = new PostPagingResponse();
		postPagingResponse.setTotalItems(paged.getTotalElements());
		postPagingResponse.setTotalPages(paged.getTotalPages());
		postPagingResponse.setPostsResponses(postsResponses);

		return postPagingResponse;

	}

	@Override
	public PostDetailResponse getOnePost(Long id) {
		Post post = postRepository.findById(id).get();

		PostDetailResponse postResponse = postMapper.toPostDetailResponse(post);
		return postResponse;
	}

	@Override
	public PostDto updateOnePost(Long id, PostRequest postRequest, MultipartFile[] images) {
		Category category = categoryRepository.findById(postRequest.getCategory()).get();
		List<Image> imageList = imageService.uploadMultiImage(images);
		Post post = postRepository.findById(id).get();

		if (post == null)
			return null;

		post.setTitle(postRequest.getTitle());
		post.setAuthor(postRequest.getAuthor());
		post.setDescription(postRequest.getDescription());
		post.setStatus(EPostStatus.UPDATE_PENDING);
		post.setDateUpdated(LocalDateTime.now());
		post.setCategory(category);
		post.setImages(imageList);
		PostDto updatedPost = postMapper.toPostDto(postRepository.save(post));

		return updatedPost;
	}

	@Override
	public PostDto deleteOnePost(Long id) {

		Post post = postRepository.findById(id).get();
		post.setStatus(EPostStatus.HIDDEN);

		PostDto deletedPost = postMapper.toPostDto(postRepository.save(post));

		return deletedPost;
	}

}
