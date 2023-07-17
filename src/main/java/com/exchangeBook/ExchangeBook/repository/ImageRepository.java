package com.exchangeBook.ExchangeBook.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exchangeBook.ExchangeBook.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long>{
	Image findByName(String name);
}
