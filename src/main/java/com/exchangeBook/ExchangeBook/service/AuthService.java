package com.exchangeBook.ExchangeBook.service;

import org.springframework.http.ResponseCookie;

import com.exchangeBook.ExchangeBook.payload.request.LoginRequest;
import com.exchangeBook.ExchangeBook.payload.request.RegisterRequest;

public interface AuthService {

	String registerNewUser(RegisterRequest registerRequest);

	ResponseCookie authenticateUser(LoginRequest loginRequest);
}
