package com.exchangeBook.ExchangeBook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exchangeBook.ExchangeBook.entity.EPostStatus;
import com.exchangeBook.ExchangeBook.entity.ERole;
import com.exchangeBook.ExchangeBook.entity.EUserStatus;
import com.exchangeBook.ExchangeBook.payload.request.UserRequest;
import com.exchangeBook.ExchangeBook.payload.response.PostPagingResponse;
import com.exchangeBook.ExchangeBook.payload.response.UserDetailResponse;
import com.exchangeBook.ExchangeBook.payload.response.UserPagingResponse;
import com.exchangeBook.ExchangeBook.payload.response.UserResponse;
import com.exchangeBook.ExchangeBook.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	UserService userService;

	/**
	 * @route GET /api/users?page=1&size=5&sort=firstName
	 * @description Get all users
	 * @param {page, size}
	 * @access
	 */
	@GetMapping("/api/users")
	public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer size) {
		UserPagingResponse userPagingResponse = userService.getAllUsers(page, size);
		return ResponseEntity.ok().body(userPagingResponse);
	}

	/**
	 * @route GET /api/users/me
	 * @description Get current user
	 * @access Login required
	 */
	@GetMapping("/api/users/me")
	public ResponseEntity<?> getCurrentUser() {
		UserDetailResponse userResponse = userService.getCurrentUser();
		return ResponseEntity.ok().body(userResponse);
	}

	/**
	 * @route GET /api/users/me/posts
	 * @description Get current user's posts
	 * @params {page, size, sort, status}
	 * @access Login required
	 */
	@GetMapping("/api/users/me/posts")
	public ResponseEntity<?> getCurrentUserPosts(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "dateCreated") String sort,
			@RequestParam(required = false) EPostStatus status) {
		PostPagingResponse userResponse = userService.getCurrentUserPosts(page, size, sort, status);
		return ResponseEntity.ok().body(userResponse);
	}

	/**
	 * @route PUT /api/users/me
	 * @description Update current user
	 * @body { firstName, lastName, phoneNumber, address:{province, district, ward,
	 *       detail}, avatar}
	 * @access Login required
	 */
	@PutMapping("/api/users/me")
	public ResponseEntity<?> updateCurentUser(@RequestBody UserRequest userRequest) {
		UserResponse userResponse = userService.updateCurrentUser(userRequest);
		return ResponseEntity.ok().body(userResponse);
	}

	/**
	 * @route GET /api/users/:id
	 * @description Get one user
	 * @var {id}
	 * @access
	 */
	@GetMapping("/api/users/{id}")
	public ResponseEntity<?> getOneUser(@PathVariable Long id) {
		UserDetailResponse userDetailResponse = userService.getOneUser(id);
		return ResponseEntity.ok().body(userDetailResponse);
	}

	/**
	 * @route GET /api/users/:id/posts
	 * @description Get one user's posts
	 * @var {id}
	 * @params {page, size, sort}
	 * @access
	 */
	@GetMapping("/api/users/{id}/posts")
	public ResponseEntity<?> getOneUserPosts(@PathVariable Long id, @RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "dateCreated") String sort) {
		PostPagingResponse userResponse = userService.getOneUserPosts(id, page, size, sort);
		return ResponseEntity.ok().body(userResponse);
	}

	/**
	 * @route PUT /api/admin/users/:id
	 * @description Update one user
	 * @params {role, status}
	 * @access Login required and has role ADMIN
	 */
	@PutMapping("/api/admin/users/{id}")
	public ResponseEntity<?> updateOneUser(@PathVariable Long id, @RequestParam(required = false) ERole role,
			@RequestParam(required = false) EUserStatus status) {
		UserResponse userResponse = userService.updateOneUser(id, role, status);
		return ResponseEntity.ok().body(userResponse);
	}

	/**
	 * @route DELETE /api/users/:id
	 * @description Lock one user account
	 * @var {id}
	 * @access Login required
	 */
	@DeleteMapping("/api/users/{id}")
	public ResponseEntity<?> deleteOneUser(@PathVariable Long id) {
		UserResponse userResponse = userService.deleteOneUser(id);
		return ResponseEntity.ok().body(userResponse);
	}

	@PutMapping("/api/accept-post/{idPost}")
	public boolean acceptThePost(@PathVariable("idPost") Long idPost) {
		boolean result = userService.acceptThePost(idPost);
		return result;
	}
}
