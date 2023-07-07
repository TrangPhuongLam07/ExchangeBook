package com.exchangeBook.ExchangeBook.repository;

import com.exchangeBook.ExchangeBook.model.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookImageRepository extends JpaRepository<BookImage, UUID> {
}
