package com.exchangeBook.ExchangeBook.payload.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

	private String title;
	private String author;
	private String description;
	private String status;
	private Long category;

//	private MultipartFile[] images;

//		private User user;

}
