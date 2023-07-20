package com.exchangeBook.ExchangeBook.payload.response;

import java.util.List;

import com.exchangeBook.ExchangeBook.dto.CategoryDto;
import com.exchangeBook.ExchangeBook.dto.ImageDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class PostDetailResponse {

	private Long id;
	private String title;
	private String author;
	private String description;
	private String status;
	private long dateCreated;
	private long dateUpdated;
	private long datePosted;
	private CategoryDto category;
	private List<ImageDto> images;

//		private User user;
}
