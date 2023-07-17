package com.exchangeBook.ExchangeBook.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
public class PostDto {
	
	@NotBlank
	private String title;
	private String author;
	private String description;
	private String status;
	private long dateCreated;
	private long dateUpdated;
	private long datePosted;
	
	@NotNull
	private Long category;
	
	private MultipartFile[] images;

//	private User user;
}
