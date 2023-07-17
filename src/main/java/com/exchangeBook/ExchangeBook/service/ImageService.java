package com.exchangeBook.ExchangeBook.service;

import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.exchangeBook.ExchangeBook.entity.Image;

public interface ImageService {

	Image uploadImage(MultipartFile image);

	Set<Image> uploadMultiImage(MultipartFile[] images);

	byte[] downloadImage(String imageName);

	List<byte[]> downloadMultiImage(String[] imagesName);
}
