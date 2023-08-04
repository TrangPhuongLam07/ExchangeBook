package com.exchangeBook.ExchangeBook.service;


import com.exchangeBook.ExchangeBook.entity.ExchangeForm;
import com.exchangeBook.ExchangeBook.payload.request.EmailMessageRequest;

public interface ExchangeBookService {
//    Book exchangeBook(Long userId, Book book);
//    ExchangeDto exchangeBook(ExchangeRequest exchangeRequest);

    boolean exchangeBook(EmailMessageRequest emailMessageRequest);
    boolean updatePoint(Long id);
}
