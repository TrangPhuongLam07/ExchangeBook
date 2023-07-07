package com.exchangeBook.ExchangeBook;

import com.exchangeBook.ExchangeBook.service.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExchangeBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeBookApplication.class, args);
	}
	@Bean
	CommandLineRunner init(FileStorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}
}
