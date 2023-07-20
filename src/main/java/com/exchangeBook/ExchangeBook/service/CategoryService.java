package com.exchangeBook.ExchangeBook.service;

import java.util.List;

import com.exchangeBook.ExchangeBook.dto.CategoryDto;

public interface CategoryService {

	CategoryDto createNewCategory(String name);

	List<CategoryDto> getAllCategories();

	CategoryDto getOneCategory(Long id);

	CategoryDto updateOneCategory(Long id, CategoryDto categoryDto);

	String deleteOneCategory(Long id);
}
