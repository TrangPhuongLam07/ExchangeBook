package com.exchangeBook.ExchangeBook.controller;

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
import com.exchangeBook.ExchangeBook.payload.request.ResetPasswordRequest;
import com.exchangeBook.ExchangeBook.payload.response.MessageResponse;
import com.exchangeBook.ExchangeBook.repository.UserRepository;
import com.exchangeBook.ExchangeBook.security.jwt.JwtUtils;
import com.exchangeBook.ExchangeBook.service.AuthService;

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
		return authService.registerNewUser(registerRequest);
	}

	/**
	 * @route GET /api/auth/register/verify?token=abc123
	 * @description This api is auto call and verify sent confirmation token in
	 *              email when user click to the VERIFY link
	 * @param {token}
	 * @access
	 */
	@GetMapping("/register/verify")
	public ResponseEntity<?> verifyToken(@RequestParam String token) {
		return authService.verifyConfirmationToken(token);
	}

	/**
	 * @route GET /api/auth/register/resend-email?email=abc@gmail.com
	 * @description Resend email including verification token link
	 * @param {email}
	 * @access
	 */
	@GetMapping("/register/resend-email")
	public ResponseEntity<?> resendVerificationEmail(@RequestParam(name = "email") String userEmail) {
		return authService.resendVerificationEmail(userEmail);
	}

	/**
	 * @route POST /api/auth/login
	 * @description User login the website
	 * @body {email, password}
	 * @access
	 */
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		return authService.authenticateUser(loginRequest);
	}

	/**
	 * @route POST /api/auth/forget-password?email=abc@gmail.com
	 * @description Send a resetting password token in an email to user
	 * @param {email}
	 * @access
	 */
	@PostMapping("/forget-password")
	public ResponseEntity<?> sendResetPasswordToken(@RequestParam(name = "email") String userEmail) {
		return authService.sendForgetPasswordToken(userEmail);
	}

	/**
	 * @route PUT /api/auth/forget-password/verify?token=abc123&password=xyz789
	 * @description User sends a resetting password token and new password to the
	 *              server to verify and update new password if the token is valid
	 * @param {token, password}
	 * @access
	 */
	@PutMapping("/forget-password/verify")
	public ResponseEntity<?> verifyResetPasswordToken(@RequestBody ResetPasswordRequest resetPasswordRequest) {
		return authService.verifyResetPasswordToken(resetPasswordRequest);
	}

	/**
	 * @route PUT /api/auth/forget-password/resend-token?email=abc@gmail.com
	 * @description Resend email including a resetting password token link
	 * @param {email}
	 * @access
	 */
	@PutMapping("/forget-password/resend-token")
	public ResponseEntity<?> resendResetPasswordToken(@RequestParam(name = "email") String userEmail) {
		return authService.resendForgetPasswordToken(userEmail);
	}

	/**
	 * @route PUT /api/auth/reset-password?currentPassword=abc&newPassword=xyz
	 * @description User resets password
	 * @param {currentPassword, newPassword}
	 * @access Login required
	 */
	@PutMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
		return authService.resetPassword(resetPasswordRequest);
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