package com.exchangeBook.ExchangeBook.payload.response;

import java.util.List;

import com.exchangeBook.ExchangeBook.dto.AddressDto;
import com.exchangeBook.ExchangeBook.dto.CategoryDto;
import com.exchangeBook.ExchangeBook.dto.ImageDto;
import com.exchangeBook.ExchangeBook.dto.PostDto;
import com.exchangeBook.ExchangeBook.entity.Address;
import com.exchangeBook.ExchangeBook.entity.ERole;
import com.exchangeBook.ExchangeBook.entity.ExchangeForm;
import com.exchangeBook.ExchangeBook.entity.Image;
import com.exchangeBook.ExchangeBook.entity.Post;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class UserDetailResponse {

	private Long id;
	
	private String email;
	
	private String firstName;
	
	private String lastName;
	
	private String phoneNumber;
	
	private String role;
	
	private int point;
	
	private ImageDto avatar;
	
	private AddressDto address;
	
	private List<PostDto> posts;
}
