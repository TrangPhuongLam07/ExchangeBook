package com.exchangeBook.ExchangeBook.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import com.exchangeBook.ExchangeBook.entity.EPostStatus;
import com.exchangeBook.ExchangeBook.entity.Post;
import com.exchangeBook.ExchangeBook.payload.request.PostRequest;

public interface PostService {

	ResponseEntity<?> createNewPost(PostRequest postRequest);

	ResponseEntity<?> getAllPosts(Integer page, Integer size, String sortBy, Specification<Post> spec);

	ResponseEntity<?> getOnePost(Long id);

	ResponseEntity<?> updateOnePost(Long id, PostRequest postRequest);

	ResponseEntity<?> updateStatusPost(Long id, EPostStatus status);

	ResponseEntity<?> deleteOnePost(Long id);

}
