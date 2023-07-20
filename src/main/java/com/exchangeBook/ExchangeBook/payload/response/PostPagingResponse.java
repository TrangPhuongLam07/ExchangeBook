package com.exchangeBook.ExchangeBook.payload.response;

import java.util.List;

import com.exchangeBook.ExchangeBook.dto.CategoryDto;
import com.exchangeBook.ExchangeBook.dto.ImageDto;

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
public class PostPagingResponse {

	private long totalItems;
	private int totalPages;
	private List<PostsResponse> postsResponses;
}
