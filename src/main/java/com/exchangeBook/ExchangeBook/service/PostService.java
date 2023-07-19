package com.exchangeBook.ExchangeBook.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import com.exchangeBook.ExchangeBook.dto.PostDto;
import com.exchangeBook.ExchangeBook.entity.Post;
import com.exchangeBook.ExchangeBook.payload.response.PostResponse;

public interface PostService {

	PostDto createNewPost(PostDto postDto, MultipartFile[] images);

	List<PostDto> getAllPosts(Integer page, Integer size, String sortBy, Specification<Post> spec);

	PostResponse getOnePost(Long id);

	PostDto updateOnePost(Long id, PostDto postDto);

	PostDto deleteOnePost(Long id);

}
