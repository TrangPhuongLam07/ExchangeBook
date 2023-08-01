package com.exchangeBook.ExchangeBook.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {

	void init();

	void storeFile(MultipartFile file);

	Path loadOne(String filename);

	Stream<Path> loadAll();

	Resource loadAsResource(String filename);

	void deleteAll();

}
