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
}
