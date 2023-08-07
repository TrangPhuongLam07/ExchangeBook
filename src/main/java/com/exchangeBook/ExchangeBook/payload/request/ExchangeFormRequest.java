package com.exchangeBook.ExchangeBook.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeFormRequest {

	private Long idPost;
	private String address;
	private String fullName;
	private String phoneNumber;
}
