package com.exchangeBook.ExchangeBook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.exchangeBook.ExchangeBook.property.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({ FileStorageProperties.class })
public class ExchangeBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeBookApplication.class, args);
	}

}
