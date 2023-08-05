package com.exchangeBook.ExchangeBook.service;

import org.springframework.data.jpa.domain.Specification;

import com.exchangeBook.ExchangeBook.dto.PostDto;
import com.exchangeBook.ExchangeBook.entity.EPostStatus;
import com.exchangeBook.ExchangeBook.entity.Post;
import com.exchangeBook.ExchangeBook.payload.request.PostRequest;
import com.exchangeBook.ExchangeBook.payload.response.PostDetailResponse;
import com.exchangeBook.ExchangeBook.payload.response.PostPagingResponse;

public interface PostService {

	PostDto createNewPost(PostRequest postRequest);

	PostPagingResponse getAllPosts(Integer page, Integer size, String sortBy, Specification<Post> spec);

	PostDetailResponse getOnePost(Long id);

	PostDto updateOnePost(Long id, PostRequest postRequest);
	
	PostDto updateStatusPost(Long id, EPostStatus status);

	PostDto deleteOnePost(Long id);

}
