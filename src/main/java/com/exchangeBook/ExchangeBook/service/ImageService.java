package com.exchangeBook.ExchangeBook.service;

import java.util.List;

import com.exchangeBook.ExchangeBook.entity.Image;

public interface ImageService {

	Image uploadImage(String base64Images);

	List<Image> uploadMultiImage(String[] base64Images);

	String downloadImage(Long id);

	List<String> downloadMultiImage(Long[] imagesId);

	void deleteImage(Long id);
	
}
