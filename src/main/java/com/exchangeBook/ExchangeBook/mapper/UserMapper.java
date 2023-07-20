package com.exchangeBook.ExchangeBook.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.exchangeBook.ExchangeBook.dto.AddressDto;
import com.exchangeBook.ExchangeBook.dto.ImageDto;
import com.exchangeBook.ExchangeBook.dto.PostDto;
import com.exchangeBook.ExchangeBook.dto.UserDto;
import com.exchangeBook.ExchangeBook.entity.User;
import com.exchangeBook.ExchangeBook.payload.response.UserDetailResponse;
import com.exchangeBook.ExchangeBook.payload.response.UsersResponse;

@Component
public class UserMapper {

	@Autowired
	AddressMapper addressMapper;

	@Autowired
	ImageMapper imageMapper;

	@Autowired
	PostMapper postMapper;

	public UserDto toUserDto(User user) {
		UserDto userDto = UserDto.builder().email(user.getEmail()).firstName(user.getFirstName())
				.lastName(user.getLastName()).phoneNumber(user.getPhoneNumber())
				.address(addressMapper.toAddressDto(user.getAddress())).build();
		return userDto;
	}

	public UserDetailResponse toUserDetailResponse(User user) {

		ImageDto avatar = imageMapper.toImageDto(user.getImage());
		AddressDto address = addressMapper.toAddressDto(user.getAddress());
		List<PostDto> posts = user.getPosts().stream().map(post -> postMapper.toPostDto(post))
				.collect(Collectors.toList());

		UserDetailResponse userDetailResponse = UserDetailResponse.builder().id(user.getId()).email(user.getEmail())
				.firstName(user.getFirstName()).lastName(user.getLastName()).phoneNumber(user.getPhoneNumber())
				.role(user.getRole().toString()).avatar(avatar).address(address).posts(posts).build();
		return userDetailResponse;
	}

	public UsersResponse toUsersResponse(User user) {
		
		ImageDto avatar = imageMapper.toImageDto(user.getImage());
		AddressDto address = addressMapper.toAddressDto(user.getAddress());

		UsersResponse usersResponse = UsersResponse.builder().id(user.getId()).email(user.getEmail())
				.firstName(user.getFirstName()).lastName(user.getLastName()).phoneNumber(user.getPhoneNumber())
				.role(user.getRole().toString()).avatar(avatar).address(address).build();
		return usersResponse;
	}
}
