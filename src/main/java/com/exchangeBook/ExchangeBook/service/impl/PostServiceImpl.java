package com.exchangeBook.ExchangeBook.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
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
import com.exchangeBook.ExchangeBook.entity.EStatus;
import com.exchangeBook.ExchangeBook.entity.Image;
import com.exchangeBook.ExchangeBook.entity.Post;
import com.exchangeBook.ExchangeBook.mapper.PostMapper;
import com.exchangeBook.ExchangeBook.payload.response.PostResponse;
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
	public PostDto createNewPost(PostDto postDto, MultipartFile[] images) {
		System.out.println(postDto);
		Category category = categoryRepository.findById(postDto.getCategory()).get();

		Set<Image> imageList = imageService.uploadMultiImage(images);

		String strMaxDatetime = "9999-12-31 23:59:59.999999";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
		LocalDateTime maxDateTime = LocalDateTime.parse(strMaxDatetime, formatter);

		Post post = new Post();
		post.setTitle(postDto.getTitle());
		post.setAuthor(postDto.getAuthor());
		post.setDescription(postDto.getDescription());
		post.setStatus(EStatus.CREATE_PENDING);
		post.setDateCreated(LocalDateTime.now());
		post.setDateUpdated(LocalDateTime.now());
		post.setDatePosted(maxDateTime);
		post.setCategory(category);
		post.setImages(imageList);
		PostDto createdPost = postMapper.toPostDto(postRepository.save(post));

		return createdPost;
	}

//	@Override
//	public List<PostResponse> getAllPosts(Integer page, Integer size, String sortBy, Specification<Post> spec) {
//		Pageable paging = PageRequest.of(page-1, size, Sort.by(Sort.Direction.DESC, sortBy));
//		Page<Post> paged = postRepository.findAll(spec, paging);
//		
//		return paged.getContent().stream().map(post -> postMapper.toPostDto(post))
//				.collect(Collectors.toList());
//
//	}

	@Override
	public PostResponse getOnePost(Long id) {
		Post post = postRepository.findById(id).get();

		PostResponse postResponse = postMapper.toPostResponse(post);
		return postResponse;
	}

	@Override
	public PostDto updateOnePost(Long id, PostDto postDto) {
		Category category = categoryRepository.findById(postDto.getCategory()).get();

		Post post = postRepository.findById(id).get();

		if (post == null)
			return null;

		post.setTitle(postDto.getTitle());
		post.setAuthor(postDto.getAuthor());
		post.setDescription(postDto.getDescription());
		post.setStatus(EStatus.UPDATE_PENDING);
		post.setDateUpdated(LocalDateTime.now());
		post.setCategory(category);
		PostDto updatedPost = postMapper.toPostDto(postRepository.save(post));

		return updatedPost;
	}

	@Override
	public PostDto deleteOnePost(Long id) {

		Post post = postRepository.findById(id).get();
		post.setStatus(EStatus.HIDDEN);

		PostDto deletedPost = postMapper.toPostDto(postRepository.save(post));

		return deletedPost;
	}

}
