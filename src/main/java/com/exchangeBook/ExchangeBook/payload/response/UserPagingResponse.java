package com.exchangeBook.ExchangeBook.payload.response;

import java.util.List;

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
public class UserPagingResponse {
	private long totalItems;
	private int totalPages;
	private List<UserResponse> usersResponses;
}
