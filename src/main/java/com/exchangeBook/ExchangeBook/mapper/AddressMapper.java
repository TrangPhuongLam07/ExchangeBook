package com.exchangeBook.ExchangeBook.mapper;

import org.springframework.stereotype.Component;

import com.exchangeBook.ExchangeBook.dto.AddressDto;
import com.exchangeBook.ExchangeBook.entity.Address;

@Component
public class AddressMapper {

	public AddressDto toAddressDto(Address address) {
		if (address == null) {
			return null;
		}
		AddressDto addressDto = AddressDto.builder().province(address.getProvince()).district(address.getDistrict())
				.ward(address.getDistrict()).detail(address.getDistrict()).build();
		return addressDto;
	}
}
