package com.exchangeBook.ExchangeBook.mapper;

import org.springframework.stereotype.Component;

import com.exchangeBook.ExchangeBook.dto.CategoryDto;
import com.exchangeBook.ExchangeBook.entity.Category;

@Component
public class CategoryMapper {

	public CategoryDto toCategoryDto(Category category) {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName(category.getName());
		return categoryDto;
	}
}
