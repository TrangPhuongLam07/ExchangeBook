package com.exchangeBook.ExchangeBook.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exchangeBook.ExchangeBook.payload.request.LoginRequest;
import com.exchangeBook.ExchangeBook.payload.request.RegisterRequest;
import com.exchangeBook.ExchangeBook.payload.response.MessageResponse;
import com.exchangeBook.ExchangeBook.repository.UserRepository;
import com.exchangeBook.ExchangeBook.security.jwt.JwtUtils;
import com.exchangeBook.ExchangeBook.service.AuthService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AuthService authService;

	@Autowired
	JwtUtils jwtUtils;
	
	/**
	 * @route POST /api/auth/register
	 * @description User register new account
	 * @body {firstName, lastName, email, password}
	 * @access
	 */
	@PostMapping("/register")
	public ResponseEntity<?> registerNewUser(@Valid @RequestBody RegisterRequest registerRequest) {
		if (userRepository.existsByEmail(registerRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already existed"));
		}
		String message = "";

		try {
			message = authService.registerNewUser(registerRequest);
		} catch (UnsupportedEncodingException | MessagingException e) {
			return ResponseEntity.internalServerError()
					.body(new MessageResponse("Server Error: Sending verification email has failed!"));
		}
		return ResponseEntity.ok().body(new MessageResponse(message));
	}

	/**
	 * @route GET /api/auth/register/verify?token=abc123
	 * @description This api is auto call and verify sent confirmation token in
	 * email when user click to the VERIFY link
	 * @param {token}
	 * @access
	 */
	@GetMapping("/register/verify")
	public ResponseEntity<?> verifyToken(@RequestParam String token) {
		if (!authService.verifyConfirmationToken(token)) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Could not verify email!"));
		}
		return ResponseEntity.ok().body(new MessageResponse("Email verified successfully!"));
	}

	/**
	 * @route GET /api/auth/register/resend-email?email=abc@gmail.com
	 * @description Resend email including verification token link
	 * @param {email}
	 * @access
	 */
	@GetMapping("/register/resend-email")
	public ResponseEntity<?> resendVerificationEmail(@RequestParam(name = "email") String userEmail) {
		String message = "";
		try {
			message = authService.resendVerificationEmail(userEmail);
		} catch (UnsupportedEncodingException | MessagingException e) {
			return ResponseEntity.internalServerError()
					.body(new MessageResponse("Server Error: Resending verification email has failed!"));
		}
		return ResponseEntity.ok().body(new MessageResponse(message));
	}

	/**
	 * @route POST /api/auth/login
	 * @description User login the website
	 * @body {email, password}
	 * @access
	 */
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		ResponseCookie jwtCookie = authService.authenticateUser(loginRequest);
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(true);
	}

	/**
	 * @route PUT /api/auth/forget-password?email=abc@gmail.com
	 * @description Send a resetting password token in an email to user
	 * @param {email}
	 * @access
	 */
	@PutMapping("/forget-password")
	public ResponseEntity<?> sendResetPasswordToken(@RequestParam(name = "email") String userEmail) {
		String message = "";
		try {
			message = authService.sendForgetPasswordToken(userEmail);
		} catch (UnsupportedEncodingException | MessagingException e) {
			return ResponseEntity.internalServerError()
					.body(new MessageResponse("Server Error: Resending verification email has failed!"));
		}
		return ResponseEntity.ok().body(new MessageResponse(message));
	}

	/**
	 * @route GET /api/auth/forget-password/verify?token=abc123
	 * @description User sends a resetting password token to the server to verify
	 * @param {token}
	 * @access
	 */
	@GetMapping("/forget-password/verify")
	public ResponseEntity<?> verifyResetPasswordToken(@RequestParam String token) {
		String message = String.valueOf(authService.verifyResetPasswordToken(token));
		return ResponseEntity.ok().body(new MessageResponse(message));
	}

	/**
	 * @route GET /api/auth/forget-password/resend-token?email=abc@gmail.com
	 * @description Resend email including a resetting password token link
	 * @param {email}
	 * @access
	 */
	@GetMapping("/forget-password/resend-token")
	public ResponseEntity<?> resendResetPasswordToken(@RequestParam(name = "email") String userEmail) {
		String message = "";
		try {
			message = authService.resendForgetPasswordToken(userEmail);
		} catch (UnsupportedEncodingException | MessagingException e) {
			return ResponseEntity.internalServerError()
					.body(new MessageResponse("Server Error: Resending verification email has failed!"));
		}
		return ResponseEntity.ok().body(new MessageResponse(message));
	}

	/**
	 * @route PUT /api/auth/reset-password?currentPassword=abc&newPassword=xyz
	 * @description User resets password
	 * @param {currentPassword, newPassword}
	 * @access Login required
	 */
	@PutMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestParam String currentPassword, @RequestParam String newPassword) {
		String message = String.valueOf(authService.resetPassword(currentPassword, newPassword));
		return ResponseEntity.ok().body(new MessageResponse(message));
	}

	/**
	 * @route POST /api/auth/logout
	 * @description User logout the website
	 * @access Login required
	 */
	@PostMapping("/logout")
	public ResponseEntity<?> logoutUser() {
		SecurityContextHolder.clearContext();
		ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
				.body(new MessageResponse("You've been signed out! "));
	}
}