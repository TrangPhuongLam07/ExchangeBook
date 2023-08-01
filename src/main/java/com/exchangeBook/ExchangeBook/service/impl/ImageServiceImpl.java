package com.exchangeBook.ExchangeBook.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.exchangeBook.ExchangeBook.entity.Image;
import com.exchangeBook.ExchangeBook.exception.StorageException;
import com.exchangeBook.ExchangeBook.exception.StorageFileNotFoundException;
import com.exchangeBook.ExchangeBook.property.FileStorageProperties;
import com.exchangeBook.ExchangeBook.repository.ImageRepository;
import com.exchangeBook.ExchangeBook.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

	@Autowired
	ImageRepository imageRepository;

	private final Path rootLocation;

	public ImageServiceImpl(FileStorageProperties properties) {
		this.rootLocation = Paths.get(properties.getUploadDir());
	}

	@Override
	public Image uploadImage(MultipartFile file) {
		Image image = null;
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file");
			}

			String fileName = System.currentTimeMillis() + "_" + RandomStringUtils.randomAlphanumeric(10);
			Path filePath = rootLocation.resolve(Paths.get(fileName)).normalize().toAbsolutePath();
			image = imageRepository.save(Image.builder().name(fileName).type(file.getContentType()).size(file.getSize())
					.path(filePath.toString()).build());

			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			}

		} catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}

		return image;
	}

	@Override
	public List<Image> uploadMultiImage(MultipartFile[] images) {
		return Arrays.asList(images).stream().map((image) -> uploadImage(image)).collect(Collectors.toList());
	}

	@Override
	public byte[] downloadImage(String fileName) {
		Image image = imageRepository.findByName(fileName);
		String filePath = image.getPath();
		byte[] images = null;
		try {
			images = Files.readAllBytes(new File(filePath).toPath());
		} catch (IOException e) {
			throw new StorageFileNotFoundException("Could not read file: " + fileName, e);
		}
		return images;
	}

	@Override
	public List<byte[]> downloadMultiImage(String[] imagesName) {
		return Arrays.asList(imagesName).stream().map((imageName) -> downloadImage(imageName))
				.collect(Collectors.toList());
	}

	@Override
	public void deleteImage(String fileName) {
		try {
			Image image = imageRepository.findByName(fileName);
			Path file = rootLocation.resolve(fileName);
			Files.deleteIfExists(file);
			imageRepository.deleteById(image.getId());
		} catch (IOException e) {
			throw new StorageException("Failed to delete file");
		}
	}

}
