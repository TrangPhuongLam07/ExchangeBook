package com.exchangeBook.ExchangeBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exchangeBook.ExchangeBook.entity.ConfirmationToken;
import com.exchangeBook.ExchangeBook.entity.ForgetPasswordToken;
import com.exchangeBook.ExchangeBook.entity.User;

public interface ForgetPasswordTokenRepository extends JpaRepository<ForgetPasswordToken, Long> {

	ForgetPasswordToken findByToken(String token);

	ForgetPasswordToken findByUser(User user);
}
