package com.exchangeBook.ExchangeBook.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.exchangeBook.ExchangeBook.exception.StorageException;
import com.exchangeBook.ExchangeBook.exception.StorageFileNotFoundException;
import com.exchangeBook.ExchangeBook.property.FileStorageProperties;
import com.exchangeBook.ExchangeBook.service.FileStorageService;

@Service
public class FileStorageServiceImpl implements FileStorageService {

	private final Path rootLocation;

	public FileStorageServiceImpl(FileStorageProperties properties) {
		this.rootLocation = Paths.get(properties.getUploadDir());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		} catch (IOException e) {
			throw new StorageException("Could not initialize storage folder", e);
		}
	}

	@Override
	public void storeFile(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file");
			}
			Path fileUrl = rootLocation.resolve(Paths.get(System.currentTimeMillis() + file.getOriginalFilename()))
					.normalize().toAbsolutePath();

			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, fileUrl, StandardCopyOption.REPLACE_EXISTING);
			}

		} catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
	}

	@Override
	public Path loadOne(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(rootLocation, 1).filter(path -> !path.equals(rootLocation))
					.map(this.rootLocation::relativize);
		} catch (IOException e) {
			throw new StorageException("Failed to read stored files");
		}
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = loadOne(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException("Could not read file" + filename);
			}
		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

}
