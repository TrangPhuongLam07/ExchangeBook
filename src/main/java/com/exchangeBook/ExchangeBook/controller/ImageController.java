package com.exchangeBook.ExchangeBook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.exchangeBook.ExchangeBook.entity.Image;
import com.exchangeBook.ExchangeBook.service.ImageService;

@RestController
@RequestMapping("/api/images")
public class ImageController {

	@Autowired
	ImageService imageService;

	@PostMapping
	public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image) {
		Image uploadedImage = imageService.uploadImage(image);
		return ResponseEntity.status(HttpStatus.OK).body(uploadedImage);
	}

	public ResponseEntity<?> downloadImage(@PathVariable String imageName) {
		byte[] downloadedImage = imageService.downloadImage(imageName);
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(downloadedImage);
	}
}
