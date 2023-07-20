package com.exchangeBook.ExchangeBook.payload.request;

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
