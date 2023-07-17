package com.exchangeBook.ExchangeBook.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exchangeBook.ExchangeBook.dto.CategoryDto;
import com.exchangeBook.ExchangeBook.entity.Category;
import com.exchangeBook.ExchangeBook.mapper.CategoryMapper;
import com.exchangeBook.ExchangeBook.repository.CategoryRepository;
import com.exchangeBook.ExchangeBook.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryMapper categoryMapper;
	
	@Autowired
	CategoryRepository categoryRepository;

	@Override
	public CategoryDto createNewCategory(CategoryDto categoryDto) {
		Category category = new Category();
		category.setName(categoryDto.getName());
		
		categoryRepository.save(category);
		
		CategoryDto created = categoryMapper.toCategoryDto(category);
		
		return created;
	}

}
