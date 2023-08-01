package com.exchangeBook.ExchangeBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exchangeBook.ExchangeBook.entity.ConfirmationToken;
import com.exchangeBook.ExchangeBook.entity.User;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

	ConfirmationToken findByToken(String token);

	ConfirmationToken findByUser(User user);
}
