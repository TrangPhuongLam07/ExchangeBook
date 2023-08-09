package com.exchangeBook.ExchangeBook.service;

import org.springframework.http.ResponseEntity;

import com.exchangeBook.ExchangeBook.entity.EPostStatus;
import com.exchangeBook.ExchangeBook.entity.ERole;
import com.exchangeBook.ExchangeBook.entity.EUserStatus;
import com.exchangeBook.ExchangeBook.payload.request.UserRequest;

public interface UserService {

//	UserPagingResponse getAllUsers(Integer page, Integer size);
	ResponseEntity<?> getAllUsers(Integer page, Integer size);

	ResponseEntity<?> getCurrentUserPosts(Integer page, Integer size, String sortBy, EPostStatus status);

	ResponseEntity<?> getOneUserPosts(Long id, Integer page, Integer size, String sortBy);

	ResponseEntity<?> getCurrentUser();

	ResponseEntity<?> getOneUser(Long id);

	ResponseEntity<?> updateOneUser(Long id, ERole role, EUserStatus status);

	ResponseEntity<?> updateCurrentUser(UserRequest userRequest);

	ResponseEntity<?> deleteOneUser(Long id);

	boolean checkPoint();

	int returnPoint();

	boolean acceptThePost(Long idPost);
}
