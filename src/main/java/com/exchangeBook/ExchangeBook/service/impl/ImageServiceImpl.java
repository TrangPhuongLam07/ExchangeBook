package com.exchangeBook.ExchangeBook.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.exchangeBook.ExchangeBook.entity.Image;
import com.exchangeBook.ExchangeBook.repository.ImageRepository;
import com.exchangeBook.ExchangeBook.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

	@Autowired
	ImageRepository imageRepository;

	@Value("${file.upload-dir}")
	private String uploadDir;

	@Override
	public Image uploadImage(MultipartFile file) {
		String filePath = uploadDir + file.getOriginalFilename();
		Image image = imageRepository.save(
				Image.builder().name(file.getOriginalFilename()).type(file.getContentType()).size(file.getSize()).path(filePath).build());
		try {
			file.transferTo(new File(filePath));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (image != null) {
			return image;
		}
		return null;
	}

	@Override
	public Set<Image> uploadMultiImage(MultipartFile[] images) {
		return Arrays.asList(images).stream().map((image) -> uploadImage(image)).collect(Collectors.toSet());
	}

	@Override
	public byte[] downloadImage(String fileName) {
		Image image = imageRepository.findByName(fileName);
		String filePath = image.getPath();
		byte[] images = null;
		try {
			images = Files.readAllBytes(new File(filePath).toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return images;
	}

	@Override
	public List<byte[]> downloadMultiImage(String[] imagesName) {
		return Arrays.asList(imagesName).stream().map((imageName) -> downloadImage(imageName))
				.collect(Collectors.toList());
	}

}
