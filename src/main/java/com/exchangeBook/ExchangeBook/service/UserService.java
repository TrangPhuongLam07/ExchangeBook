package com.exchangeBook.ExchangeBook.service;

import org.springframework.web.multipart.MultipartFile;

import com.exchangeBook.ExchangeBook.dto.UserDto;
import com.exchangeBook.ExchangeBook.payload.request.UserRequest;
import com.exchangeBook.ExchangeBook.payload.response.UserDetailResponse;
import com.exchangeBook.ExchangeBook.payload.response.UserPagingResponse;
import com.exchangeBook.ExchangeBook.payload.response.UserResponse;

public interface UserService {

	UserPagingResponse getAllUsers(Integer page, Integer size);
	
	UserDetailResponse getCurrentUser();
	
	UserDetailResponse getOneUser(Long id);
	
	UserResponse updateOneUser(Long id, UserRequest userRequest, MultipartFile image);
	
	UserResponse deleteOneUser(Long id);
	boolean checkPoint(Long userId);
}
