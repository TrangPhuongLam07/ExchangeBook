package com.exchangeBook.ExchangeBook.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.exchangeBook.ExchangeBook.dto.ImageDto;
import com.exchangeBook.ExchangeBook.entity.Image;
import com.exchangeBook.ExchangeBook.service.ImageService;

@Component
public class ImageMapper {

	@Autowired
	ImageService imageService;

	public ImageDto toImageDto(Image image) {

		return ImageDto.builder().name(image.getName()).contentType(image.getContentType())
				.data(imageService.downloadImage(image.getId())).build();
	}
}
