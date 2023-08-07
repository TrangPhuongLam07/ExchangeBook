package com.exchangeBook.ExchangeBook.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.exchangeBook.ExchangeBook.service.FileStorageService;

@RestController
@RequestMapping("/api/files/")
public class FileUploadController {

	@Autowired
	FileStorageService fileStorageService;

	@GetMapping
	public ResponseEntity<?> getAllFileNames() {
		List<String> allFileNames = fileStorageService.loadAll().map(file -> file.getFileName().toString())
				.collect(Collectors.toList());
		return ResponseEntity.ok(allFileNames);
	}

	@GetMapping("/{filename:.+}")
	public ResponseEntity<?> downloadFile(@PathVariable String filename) {
		Resource file = fileStorageService.loadAsResource(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@PostMapping
	public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file) {
		fileStorageService.storeFile(file);
		return ResponseEntity.ok("Upload file successfully!");
	}
}
