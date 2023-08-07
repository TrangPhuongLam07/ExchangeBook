package com.exchangeBook.ExchangeBook.service;


import com.exchangeBook.ExchangeBook.payload.request.EmailMessageRequest;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface ExchangeBookService {
//    Book exchangeBook(Long userId, Book book);
//    ExchangeDto exchangeBook(ExchangeRequest exchangeRequest);

    boolean exchangeBook(EmailMessageRequest emailMessageRequest) throws MessagingException, UnsupportedEncodingException;
    boolean updatePoint(Long id);
}
