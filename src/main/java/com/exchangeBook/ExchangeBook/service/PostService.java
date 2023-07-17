package com.exchangeBook.ExchangeBook.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.exchangeBook.ExchangeBook.dto.PostDto;
import com.exchangeBook.ExchangeBook.payload.response.PostResponse;

public interface PostService {

	PostDto createNewPost(PostDto postDto, MultipartFile[] images);

	List<PostDto> getAllPosts();

	PostResponse getOnePost(Long id);

	PostDto updateOnePost(Long id, PostDto postDto);

	PostDto deleteOnePost(Long id);

}
