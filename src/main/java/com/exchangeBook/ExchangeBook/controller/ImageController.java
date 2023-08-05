package com.exchangeBook.ExchangeBook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exchangeBook.ExchangeBook.service.ImageService;

@RestController
@RequestMapping("/api/images")
public class ImageController {

	@Autowired
	ImageService imageService;

//	@PostMapping
//	public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image) {
//		Image uploadedImage = imageService.uploadImage(image);
//		return ResponseEntity.status(HttpStatus.OK).body(uploadedImage);
//	}
//
//	public ResponseEntity<?> downloadImage(@PathVariable String imageName) {
//		byte[] downloadedImage = imageService.downloadImage(imageName);
//		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(downloadedImage);
//	}
}
