package com.exchangeBook.ExchangeBook.payload.response;

import java.util.List;

import com.exchangeBook.ExchangeBook.dto.AddressDto;
import com.exchangeBook.ExchangeBook.dto.CategoryDto;
import com.exchangeBook.ExchangeBook.dto.ImageDto;
import com.exchangeBook.ExchangeBook.dto.PostDto;

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
public class UsersResponse {
	private Long id;

	private String email;

	private String firstName;

	private String lastName;

	private String phoneNumber;

	private String role;

	private int point;

	private ImageDto avatar;

	private AddressDto address;

}
