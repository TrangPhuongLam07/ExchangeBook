package com.exchangeBook.ExchangeBook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.exchangeBook.ExchangeBook.dto.UserDto;
import com.exchangeBook.ExchangeBook.payload.request.UserRequest;
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
	 * @route GET /users?page=1&size=5&sort=firstName
	 * @description Get all users
	 * @body
	 * @access
	 */
	@GetMapping
	public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer size) {
		UserPagingResponse userPagingResponse = userService.getAllUsers(page, size);
		return ResponseEntity.ok().body(userPagingResponse);
	}

	/**
	 * @route GET /users/me
	 * @description Get current user
	 * @body
	 * @access Login required
	 */
	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser() {
		UserDetailResponse userResponse = userService.getCurrentUser();
		return ResponseEntity.ok().body(userResponse);
	}

	/**
	 * @route GET /users/:id
	 * @description Get one user
	 * @body
	 * @access
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getOneUser(@PathVariable Long id) {
		UserDetailResponse userDetailResponse = userService.getOneUser(id);
		return ResponseEntity.ok().body(userDetailResponse);
	}

	/**
	 * @route PUT /users/:id
	 * @description Update one user
	 * @body {email, firstName, lastName, phoneNumber, address:{province, district,
	 *       ward, detail}}
	 * @access Login required
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateOneUser(@PathVariable Long id, @RequestPart UserRequest userRequest,
			@RequestPart(required = false) MultipartFile avatar) {
		UserResponse userResponse = userService.updateOneUser(id, userRequest, avatar);
		return ResponseEntity.ok().body(userResponse);
	}

	/**
	 * @route DELETE /users/:id
	 * @description Lock one user account
	 * @body
	 * @access Login required
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOneUser(@PathVariable Long id) {
		UserResponse userResponse = userService.deleteOneUser(id);
		return ResponseEntity.ok().body(userResponse);
	}
}
