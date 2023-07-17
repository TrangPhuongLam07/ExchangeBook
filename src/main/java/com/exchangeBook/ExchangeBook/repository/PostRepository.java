package com.exchangeBook.ExchangeBook.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exchangeBook.ExchangeBook.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

}
