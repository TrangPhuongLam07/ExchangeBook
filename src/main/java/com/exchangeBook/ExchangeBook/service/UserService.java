package com.exchangeBook.ExchangeBook.service;

import org.springframework.http.ResponseEntity;

import com.exchangeBook.ExchangeBook.entity.EPostStatus;
import com.exchangeBook.ExchangeBook.entity.ERole;
import com.exchangeBook.ExchangeBook.entity.EUserStatus;
import com.exchangeBook.ExchangeBook.payload.request.UserRequest;

public interface UserService {

<<<<<<< HEAD
	UserPagingResponse getAllUsers(Integer page, Integer size);

	PostPagingResponse getCurrentUserPosts(Integer page, Integer size, String sortBy, EPostStatus status);

	PostPagingResponse getOneUserPosts(Long id, Integer page, Integer size, String sortBy);

	UserDetailResponse getCurrentUser();

	UserDetailResponse getOneUser(Long id);

	UserResponse updateOneUser(Long id, ERole role, EUserStatus status);

	UserResponse updateCurrentUser(UserRequest userRequest);

	UserResponse deleteOneUser(Long id);

=======
	ResponseEntity<?> getAllUsers(Integer page, Integer size);

	ResponseEntity<?> getCurrentUserPosts(Integer page, Integer size, String sortBy, EPostStatus status);

	ResponseEntity<?> getOneUserPosts(Long id, Integer page, Integer size, String sortBy);

	ResponseEntity<?> getCurrentUser();

	ResponseEntity<?> getOneUser(Long id);

	ResponseEntity<?> updateOneUser(Long id, ERole role, EUserStatus status);

	ResponseEntity<?> updateCurrentUser(UserRequest userRequest);

	ResponseEntity<?> deleteOneUser(Long id);

>>>>>>> ca46d9956859d6ed82fcf000d6f659662508f924
	boolean checkPoint();

	int returnPoint();

	boolean acceptThePost(Long idPost);
}
