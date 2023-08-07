package com.exchangeBook.ExchangeBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exchangeBook.ExchangeBook.entity.ForgetPasswordToken;
import com.exchangeBook.ExchangeBook.entity.User;

@Repository
public interface ForgetPasswordTokenRepository extends JpaRepository<ForgetPasswordToken, Long> {

	ForgetPasswordToken findByToken(String token);

	ForgetPasswordToken findByUser(User user);
}
