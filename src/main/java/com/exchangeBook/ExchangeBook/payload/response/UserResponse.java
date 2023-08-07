package com.exchangeBook.ExchangeBook.payload.response;

import com.exchangeBook.ExchangeBook.dto.AddressDto;
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
public class UserResponse {
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
