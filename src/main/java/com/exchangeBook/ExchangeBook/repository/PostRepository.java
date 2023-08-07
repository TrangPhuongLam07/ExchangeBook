package com.exchangeBook.ExchangeBook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.exchangeBook.ExchangeBook.entity.EPostStatus;
import com.exchangeBook.ExchangeBook.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
	Page<Post> findAll(Pageable paging);

	Page<Post> findAll(Specification<Post> spec, Pageable paging);

	Page<Post> findAllByUserId(Long id, Pageable paging);

	@Query("select p from Post p where p.user.id = :id and (:status is null or p.status like :status)")
	Page<Post> findAllByUserIdAndStatusLike(Long id, EPostStatus status, Pageable paging);
}
