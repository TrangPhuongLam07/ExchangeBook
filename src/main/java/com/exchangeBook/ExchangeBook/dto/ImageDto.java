package com.exchangeBook.ExchangeBook.dto;

import java.util.List;

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
public class ImageDto {
	
	private String name;
	
	private String type;
	
	private long size;
	
	private byte[] data;
}
