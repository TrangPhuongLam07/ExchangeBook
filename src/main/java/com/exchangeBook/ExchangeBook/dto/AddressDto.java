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
public class AddressDto {

	private String province;
	private String district;
	private String ward;
	private String detail;
	
}
