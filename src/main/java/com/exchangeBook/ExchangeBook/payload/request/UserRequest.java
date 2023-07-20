package com.exchangeBook.ExchangeBook.payload.request;

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

	private String email;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private ERole role;
	private Long address;
}
