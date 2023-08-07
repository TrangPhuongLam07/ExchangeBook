package com.exchangeBook.ExchangeBook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exchangeBook.ExchangeBook.dto.PostDto;
import com.exchangeBook.ExchangeBook.entity.EPostStatus;
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
public class PostController {
	@Autowired
	PostService postService;

	/**
	 * @route POST /api/posts
	 * @description Create a new post
	 * @body {title, author, category, description, status, images}
	 * @access Login required
	 */
	@PostMapping("/api/posts")
	public ResponseEntity<?> createNewPost(@RequestBody PostRequest postRequest) {
		PostDto dto = postService.createNewPost(postRequest);
		return ResponseEntity.ok().body(dto);
	}

	/**
	 * @route GET
	 *        /api/posts?page=1&size=10&sort=dateCreated&title=post&author&category=mystery
	 * @description Get all approved posts and some filters
	 * @params {page, size, sort, title, author, category}
	 * @access
	 */
	@Transactional
	@GetMapping("/api/posts")
	public ResponseEntity<?> getApprovedPosts(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "datePosted") String sort,
			@And({ @Spec(path = "title", params = "title", spec = Like.class),
					@Spec(path = "author", params = "author", spec = Like.class),
					@Spec(path = "status", constVal = "APPROVED", spec = Equal.class),
					@Spec(path = "category.name", params = "category", spec = Like.class) }) Specification<Post> postSpec) {
		PostPagingResponse postPagingResponse = postService.getAllPosts(page, size, sort, postSpec);
		return ResponseEntity.ok().body(postPagingResponse);
	}

	/**
	 * @route GET
	 *        /api/admin/posts?page=1&size=10&sort=dateCreated&title=post&author&status=APPROVED&category=mystery
	 * @description Get all posts for admin
	 * @params {page, size, sort, title, author, status, category}
	 * @access Login required and has role ADMIN
	 */
	@Transactional
	@GetMapping("/api/admin/posts")
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
	 * @route GET /api/posts/search?page=1&size=10&sort=dateCreated&keyword=post
	 * @description Search on approved posts when the given keyword match {title,
	 *              author, description, category.name}
	 * @params {page, size, sort, title, author, category}
	 * @access
	 */
	@Transactional
	@GetMapping("/api/posts/search")
	public ResponseEntity<?> searchInPosts(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "dateCreated") String sortBy,
			@Or({ @Spec(path = "title", params = "keyword", spec = Like.class),
					@Spec(path = "author", params = "keyword", spec = Like.class),
					@Spec(path = "description", params = "keyword", spec = Like.class),
					@Spec(path = "status", constVal = "APPROVED", spec = Equal.class),
					@Spec(path = "category.name", params = "keyword", spec = Like.class) }) Specification<Post> postSpec) {
		PostPagingResponse postPagingResponse = postService.getAllPosts(page, size, sortBy, postSpec);
		return ResponseEntity.ok().body(postPagingResponse);
	}

	/**
	 * @route GET /api/posts/:id
	 * @description Get single post
	 * @var {id}
	 * @access
	 */
	@GetMapping("/api/posts/{id}")
	public ResponseEntity<?> getOnePost(@PathVariable Long id) {
		PostDetailResponse response = postService.getOnePost(id);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * @route PUT /api/posts/:id
	 * @description Update a post
	 * @var {id}
	 * @body {title, author, category, description, images}
	 * @access Login required
	 */
	@PutMapping("/api/posts/{id}")
	public ResponseEntity<?> updateOnePost(@PathVariable Long id, @RequestBody PostRequest postRequest) {
		PostDto dto = postService.updateOnePost(id, postRequest);
		return ResponseEntity.ok().body(dto);
	}

	/**
	 * @route PUT /api/admin/posts/:id
	 * @description Update a post
	 * @params {status}
	 * @access Login required and has role ADMIN
	 */
	@PutMapping("/api/admin/posts/{id}")
	public ResponseEntity<?> updateStatusPost(@PathVariable Long id, @RequestParam EPostStatus status) {
		PostDto dto = postService.updateStatusPost(id, status);
		return ResponseEntity.ok().body(dto);
	}

	/**
	 * @route DELETE /posts/:id
	 * @description Hide a post
	 * @var {id}
	 * @access Login required
	 */
	@DeleteMapping("/api/posts/{id}")
	public ResponseEntity<?> deleteOnePost(@PathVariable Long id) {
		PostDto dto = postService.deleteOnePost(id);
		return ResponseEntity.ok().body(dto);
	}
}
