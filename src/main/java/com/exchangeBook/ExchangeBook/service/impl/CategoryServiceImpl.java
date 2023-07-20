package com.exchangeBook.ExchangeBook.service.impl;

import java.util.List;
import java.util.stream.Collectors;

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
	public CategoryDto createNewCategory(String name) {
		Category category = new Category();
		category.setName(name);

		categoryRepository.save(category);

		CategoryDto created = categoryMapper.toCategoryDto(category);

		return created;
	}

	@Override
	public List<CategoryDto> getAllCategories() {
		List<CategoryDto> categoryDtos = categoryRepository.findAll().stream()
				.map(category -> categoryMapper.toCategoryDto(category)).collect(Collectors.toList());
		return categoryDtos;
	}

	@Override
	public CategoryDto getOneCategory(Long id) {
		CategoryDto categoryDto = categoryMapper.toCategoryDto(categoryRepository.findById(id).get());
		return categoryDto;
	}

	@Override
	public CategoryDto updateOneCategory(Long id, CategoryDto categoryDto) {
		Category category = categoryRepository.findById(id).get();
		category.setName(categoryDto.getName());

		Category updated = categoryRepository.save(category);

		return categoryMapper.toCategoryDto(updated);
	}

	@Override
	public String deleteOneCategory(Long id) {
		categoryRepository.deleteById(id);
		return "Delete category successfully";
	}

}
