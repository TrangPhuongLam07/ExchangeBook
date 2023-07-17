package com.exchangeBook.ExchangeBook.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exchangeBook.ExchangeBook.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
	Optional<Category> findById(Long id);
}
