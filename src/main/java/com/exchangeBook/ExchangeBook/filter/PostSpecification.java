package com.exchangeBook.ExchangeBook.filter;

import org.springframework.data.jpa.domain.Specification;

import com.exchangeBook.ExchangeBook.entity.Post;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class PostSpecification implements Specification<Post>{

	
	
	@Override
	public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		// TODO Auto-generated method stub
		return null;
	}

}
