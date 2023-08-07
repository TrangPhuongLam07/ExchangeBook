package com.exchangeBook.ExchangeBook.mapper;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.exchangeBook.ExchangeBook.dto.CategoryDto;
import com.exchangeBook.ExchangeBook.dto.ImageDto;
import com.exchangeBook.ExchangeBook.dto.PostDto;
import com.exchangeBook.ExchangeBook.entity.Post;
import com.exchangeBook.ExchangeBook.payload.response.PostDetailResponse;
import com.exchangeBook.ExchangeBook.payload.response.PostResponse;
import com.exchangeBook.ExchangeBook.repository.CategoryRepository;

@Component
public class PostMapper {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ImageMapper imageMapper;

	@Autowired
	CategoryMapper categoryMapper;

	public PostDto toPostDto(Post post) {
		PostDto postDto = new PostDto();
		postDto.setTitle(post.getTitle());
		postDto.setAuthor(post.getAuthor());
		postDto.setDescription(post.getDescription());
		postDto.setStatus(post.getStatus().toString());
		postDto.setDateCreated(post.getDateCreated().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		postDto.setDateUpdated(post.getDateUpdated().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		postDto.setDatePosted(post.getDatePosted().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		postDto.setCategory(categoryMapper.toCategoryDto(post.getCategory()));

		return postDto;
	}

	public PostDetailResponse toPostDetailResponse(Post post) {

		List<ImageDto> images = post.getImages().stream().map(image -> imageMapper.toImageDto(image))
				.collect(Collectors.toList());
		CategoryDto category = categoryMapper.toCategoryDto(post.getCategory());
		PostDetailResponse postDetailResponse = PostDetailResponse.builder().id(post.getId()).title(post.getTitle())
				.author(post.getAuthor()).description(post.getDescription()).status(post.getStatus().toString())
				.dateCreated(post.getDateCreated().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
				.dateUpdated(post.getDateUpdated().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
				.datePosted(post.getDatePosted().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
				.category(category).images(images).build();

		return postDetailResponse;
	}

	public PostResponse toPostsResponse(Post post) {

		ImageDto imageDto = imageMapper.toImageDto(post.getImages().get(0));

		CategoryDto category = categoryMapper.toCategoryDto(post.getCategory());

		PostResponse postsResponse = PostResponse.builder().id(post.getId()).title(post.getTitle())
				.author(post.getAuthor()).description(post.getDescription()).status(post.getStatus().toString())
				.dateCreated(post.getDateCreated().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
				.dateUpdated(post.getDateUpdated().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
				.datePosted(post.getDatePosted().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
				.category(category).image(imageDto).build();
		return postsResponse;
	}
}
