package com.exchangeBook.ExchangeBook.service;

import java.io.UnsupportedEncodingException;

import org.springframework.http.ResponseCookie;

import com.exchangeBook.ExchangeBook.payload.request.LoginRequest;
import com.exchangeBook.ExchangeBook.payload.request.RegisterRequest;

import jakarta.mail.MessagingException;

public interface AuthService {

	String registerNewUser(RegisterRequest registerRequest) throws UnsupportedEncodingException, MessagingException;

	ResponseCookie authenticateUser(LoginRequest loginRequest);
	
	boolean verifyConfirmationToken(String token);
	
	String resendVerificationEmail(String userEmail) throws UnsupportedEncodingException, MessagingException;
	
	String sendForgetPasswordToken(String email) throws UnsupportedEncodingException, MessagingException;
	
	String resendForgetPasswordToken(String email) throws UnsupportedEncodingException, MessagingException;

	boolean verifyResetPasswordToken(String resetPasswordToken);

	boolean resetPassword(String currentPassword, String newPassword);

}
