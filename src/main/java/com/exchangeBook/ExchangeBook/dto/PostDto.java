package com.exchangeBook.ExchangeBook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

	private String title;
	private String author;
	private String description;
	private String status;
	private long dateCreated;
	private long dateUpdated;
	private long datePosted;

	private CategoryDto category;

//	private MultipartFile[] images;

//	private User user;
}
