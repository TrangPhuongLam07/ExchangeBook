package com.exchangeBook.ExchangeBook.dto;

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
public class UserDto {

	private String firstName;

	private String lastName;

	private String phoneNumber;

	private AddressDto address;

}
