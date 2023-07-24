package com.exchangeBook.ExchangeBook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exchangeBook.ExchangeBook.dto.CategoryDto;
import com.exchangeBook.ExchangeBook.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	/**
	 * @route POST /categories
	 * @description Create a new category
	 * @body {name}
	 * @access Login required
	 */
	@PostMapping
	public ResponseEntity<?> createNewCategory(@RequestBody CategoryDto categoryDto) {
		CategoryDto dto = categoryService.createNewCategory(categoryDto);
		return ResponseEntity.ok().body(dto);
	}

	/**
	 * @route GET /categories
	 * @description Get all categories
	 * @body
	 * @access
	 */
	@GetMapping
	public ResponseEntity<?> getAllCategories() {
		List<CategoryDto> dtos = categoryService.getAllCategories();
		return ResponseEntity.ok().body(dtos);
	}

	/**
	 * @route GET /categories/:id
	 * @description Get a category
	 * @body
	 * @access
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getOneCategory(@PathVariable Long id) {
		CategoryDto dto = categoryService.getOneCategory(id);
		return ResponseEntity.ok().body(dto);
	}

	/**
	 * @route PUT /categories/:id
	 * @description Update a category
	 * @body {name}
	 * @access Login required
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateOneCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
		CategoryDto dto = categoryService.updateOneCategory(id, categoryDto);
		return ResponseEntity.ok().body(dto);
	}

	/**
	 * @route DELETE /categories/:id
	 * @description Delete a category
	 * @body
	 * @access Login required
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOneCategory(@PathVariable Long id) {
		String message = categoryService.deleteOneCategory(id);
		return ResponseEntity.ok().body(message);
	}

}
