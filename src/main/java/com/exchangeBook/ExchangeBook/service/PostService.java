package com.exchangeBook.ExchangeBook.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import com.exchangeBook.ExchangeBook.dto.PostDto;
import com.exchangeBook.ExchangeBook.entity.Post;
import com.exchangeBook.ExchangeBook.payload.request.PostRequest;
import com.exchangeBook.ExchangeBook.payload.response.PostDetailResponse;
import com.exchangeBook.ExchangeBook.payload.response.PostPagingResponse;

public interface PostService {

	PostDto createNewPost(PostRequest postRequest, MultipartFile[] images);

	PostPagingResponse getAllPosts(Integer page, Integer size, String sortBy, Specification<Post> spec);

	PostDetailResponse getOnePost(Long id);

	PostDto updateOnePost(Long id, PostRequest postRequest, MultipartFile[] images);

	PostDto deleteOnePost(Long id);

}
