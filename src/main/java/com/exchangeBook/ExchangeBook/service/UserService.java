package com.exchangeBook.ExchangeBook.service;

import org.springframework.web.multipart.MultipartFile;

import com.exchangeBook.ExchangeBook.dto.UserDto;
import com.exchangeBook.ExchangeBook.payload.request.UserRequest;
import com.exchangeBook.ExchangeBook.payload.response.UserDetailResponse;
import com.exchangeBook.ExchangeBook.payload.response.UserPagingResponse;

public interface UserService {

//	UserDto createNewUser(UserRequest userRequest);
	
	UserPagingResponse getAllUsers(Integer page, Integer size);
	
	UserDetailResponse getOneUser(Long id);
	
	UserDto updateOneUser(Long id, UserRequest userRequest, MultipartFile image);
	
	UserDto deleteOneUser(Long id);
}
