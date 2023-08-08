package com.exchangeBook.ExchangeBook.repository;


import com.exchangeBook.ExchangeBook.entity.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
}
