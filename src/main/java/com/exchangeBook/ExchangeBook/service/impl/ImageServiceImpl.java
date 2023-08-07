package com.exchangeBook.ExchangeBook.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

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
	public Image uploadImage(String base64Images) {
		String[] imageBase64 = base64Images.split(",");
		String contentType = imageBase64[0].split("[/;]")[1];
		String dataBase64 = imageBase64[1];

		String fileName = System.currentTimeMillis() + "_" + RandomStringUtils.randomAlphanumeric(10) + "."
				+ contentType;
		Path filePath = rootLocation.resolve(Paths.get(fileName)).normalize().toAbsolutePath();

		try {
			byte[] fileByte = Base64.getDecoder().decode(dataBase64);

			ByteArrayInputStream bais = new ByteArrayInputStream(fileByte);
			BufferedImage bufferImage = ImageIO.read(bais);

			File file = new File(filePath.toString());
			ImageIO.write(bufferImage, contentType, file);

			Image image = imageRepository.save(Image.builder().name(fileName).contentType(contentType)
					.size(file.length()).path(filePath.toString()).build());
			return image;
		} catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
	}

	@Override
	public List<Image> uploadMultiImage(String[] base64Images) {
		return Arrays.asList(base64Images).stream().map(image -> uploadImage(image)).collect(Collectors.toList());
	}

	@Override
	public String downloadImage(Long id) {
		Image image = imageRepository.findById(id).get();
		String filePath = image.getPath();
		byte[] images = null;
		String data = "";
		try {
			images = Files.readAllBytes(new File(filePath).toPath());
			data = Base64.getEncoder().encodeToString(images);
		} catch (IOException e) {
			throw new StorageFileNotFoundException("Could not read file: " + id, e);
		}
		return data;
	}

	@Override
	public List<String> downloadMultiImage(Long[] imagesId) {
		return Arrays.asList(imagesId).stream().map((imageId) -> downloadImage(imageId)).collect(Collectors.toList());
	}

	@Override
	public void deleteImage(Long id) {
		try {
			Image image = imageRepository.findById(id).get();
			Path file = rootLocation.resolve(image.getName());
			Files.deleteIfExists(file);
			imageRepository.deleteById(image.getId());
		} catch (IOException e) {
			throw new StorageException("Failed to delete file");
		}
	}
}
