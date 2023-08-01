package com.exchangeBook.ExchangeBook.payload.request;

import java.util.List;

import com.exchangeBook.ExchangeBook.dto.AddressDto;
import com.exchangeBook.ExchangeBook.dto.ImageDto;
import com.exchangeBook.ExchangeBook.dto.PostDto;
import com.exchangeBook.ExchangeBook.entity.ERole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

	private String firstName;
	private String lastName;
	private String phoneNumber;
	private ERole role;
	private Long address;
}
