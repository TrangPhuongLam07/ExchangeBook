package com.exchangeBook.ExchangeBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exchangeBook.ExchangeBook.entity.ExchangeForm;
import com.exchangeBook.ExchangeBook.entity.ExchangeFormId;

public interface ExchangeFormRepository extends JpaRepository<ExchangeForm, ExchangeFormId> {

}
