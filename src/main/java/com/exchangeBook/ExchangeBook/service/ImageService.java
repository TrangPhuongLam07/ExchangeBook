package com.exchangeBook.ExchangeBook.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.exchangeBook.ExchangeBook.entity.Image;

public interface ImageService {

	Image uploadImage(MultipartFile image);

	List<Image> uploadMultiImage(MultipartFile[] images);

	byte[] downloadImage(String imageName);

	List<byte[]> downloadMultiImage(String[] imagesName);

	void deleteImage(String fileName);
}
