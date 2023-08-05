package com.exchangeBook.ExchangeBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exchangeBook.ExchangeBook.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{
	Image findByName(String name);
}
