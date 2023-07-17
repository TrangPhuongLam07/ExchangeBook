package com.exchangeBook.ExchangeBook.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.exchangeBook.ExchangeBook.dto.PostDto;
import com.exchangeBook.ExchangeBook.payload.response.PostResponse;
import com.exchangeBook.ExchangeBook.service.PostService;

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
	@PostMapping(/* consumes = MediaType.MULTIPART_FORM_DATA_VALUE */)
	public ResponseEntity<?> createNewPost(@RequestPart PostDto postDto, @RequestPart MultipartFile[] images) {
		PostDto dto = postService.createNewPost(postDto, images);
		return ResponseEntity.ok().body(dto);
	}

	/**
	 * @route GET /posts
	 * @description Get all posts
	 * @body
	 * @access
	 */
	@GetMapping
	public ResponseEntity<?> getAllPosts() {
		List<PostDto> dto = postService.getAllPosts();
		return ResponseEntity.ok().body(dto);
	}

	/**
	 * @route GET /posts/:id
	 * @description Get single post
	 * @body
	 * @access
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getOnePost(@PathVariable Long id) {
		PostResponse response = postService.getOnePost(id);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * @route PUT /posts/:id
	 * @description Update a post
	 * @body {title, author, category, description, status, images}
	 * @access Login required
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateOnePost(@PathVariable Long id, @RequestBody PostDto postDto) {
		PostDto dto = postService.updateOnePost(id, postDto);
		return ResponseEntity.ok().body(dto);
	}

	/**
	 * @route DELETE /posts:id
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
