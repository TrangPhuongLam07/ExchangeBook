package com.exchangeBook.ExchangeBook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.exchangeBook.ExchangeBook.dto.PostDto;
import com.exchangeBook.ExchangeBook.entity.Post;
import com.exchangeBook.ExchangeBook.payload.request.PostRequest;
import com.exchangeBook.ExchangeBook.payload.response.PostDetailResponse;
import com.exchangeBook.ExchangeBook.payload.response.PostPagingResponse;
import com.exchangeBook.ExchangeBook.service.PostService;

import jakarta.transaction.Transactional;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	@Autowired
	PostService postService;

	/**
	 * @route POST /posts
	 * @description Create a new post
	 * @body {title, author, category, description, status, images}
	 * @access Login required
	 */
	@PostMapping
	public ResponseEntity<?> createNewPost(@RequestPart PostRequest postRequest, @RequestPart MultipartFile[] images) {
		PostDto dto = postService.createNewPost(postRequest, images);
		return ResponseEntity.ok().body(dto);
	}

	/**
	 * @route GET /posts?page=1&size=10&sort=dateCreated&title=post&author&status=APPROVED&category=mystery
	 * @description Get all posts
	 * @body
	 * @access
	 */
	@Transactional
	@GetMapping
	public ResponseEntity<?> getAllPosts(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "dateCreated") String sort,
			@And({ @Spec(path = "title", params = "title", spec = Like.class),
					@Spec(path = "author", params = "author", spec = Like.class),
					@Spec(path = "status", params = "status", spec = Equal.class),
					@Spec(path = "category.name", params = "category", spec = Like.class) }) Specification<Post> postSpec) {
		PostPagingResponse postPagingResponse = postService.getAllPosts(page, size, sort, postSpec);
		return ResponseEntity.ok().body(postPagingResponse);
	}

	/**
	 * @route GET /posts/search?page=1&size=10&sort=dateCreated&keyword=post
	 * @description Search in posts when the given keyword match {title, author, description, category.name}
	 * @body
	 * @access
	 */
	@Transactional
	@GetMapping("/search")
	public ResponseEntity<?> searchInPosts(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "dateCreated") String sortBy,
			@Or({ @Spec(path = "title", params = "keyword", spec = Like.class),
					@Spec(path = "author", params = "keyword", spec = Like.class),
					@Spec(path = "description", params = "keyword", spec = Like.class),
					@Spec(path = "category.name", params = "keyword", spec = Like.class) }) Specification<Post> postSpec) {
		PostPagingResponse postPagingResponse = postService.getAllPosts(page, size, sortBy, postSpec);
		return ResponseEntity.ok().body(postPagingResponse);
	}

	/**
	 * @route GET /posts/:id
	 * @description Get single post
	 * @body
	 * @access
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getOnePost(@PathVariable Long id) {
		PostDetailResponse response = postService.getOnePost(id);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * @route PUT /posts/:id
	 * @description Update a post
	 * @body {title, author, category, description, status, images}
	 * @access Login required
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateOnePost(@PathVariable Long id, @RequestPart PostRequest postRequest, @RequestPart MultipartFile[] images) {
		PostDto dto = postService.updateOnePost(id, postRequest, images);
		return ResponseEntity.ok().body(dto);
	}

	/**
	 * @route DELETE /posts/:id
	 * @description Hide a post
	 * @body
	 * @access Login required
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOnePost(@PathVariable Long id) {
		PostDto dto = postService.deleteOnePost(id);
		return ResponseEntity.ok().body(dto);
	}
}
