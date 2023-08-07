package com.exchangeBook.ExchangeBook.service;

import com.exchangeBook.ExchangeBook.entity.EPostStatus;
import com.exchangeBook.ExchangeBook.entity.ERole;
import com.exchangeBook.ExchangeBook.entity.EUserStatus;
import com.exchangeBook.ExchangeBook.payload.request.UserRequest;
import com.exchangeBook.ExchangeBook.payload.response.PostPagingResponse;
import com.exchangeBook.ExchangeBook.payload.response.UserDetailResponse;
import com.exchangeBook.ExchangeBook.payload.response.UserPagingResponse;
import com.exchangeBook.ExchangeBook.payload.response.UserResponse;

public interface UserService {

	UserPagingResponse getAllUsers(Integer page, Integer size);
	
	PostPagingResponse getCurrentUserPosts(Integer page, Integer size, String sortBy, EPostStatus status);
	
	PostPagingResponse getOneUserPosts(Long id, Integer page, Integer size, String sortBy);
	
	UserDetailResponse getCurrentUser();
	
	UserDetailResponse getOneUser(Long id);
	
	UserResponse updateOneUser(Long id, ERole role, EUserStatus status);
	
	UserResponse updateCurrentUser(UserRequest userRequest);

	UserResponse deleteOneUser(Long id);
	boolean checkPoint();
	int returnPoint();
	boolean acceptThePost(Long idPost);
}
