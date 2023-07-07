package com.exchangeBook.ExchangeBook.service;

import com.exchangeBook.ExchangeBook.model.Book;
import com.exchangeBook.ExchangeBook.modelBase.BookBase;

import java.util.UUID;

public interface BookService {
public Book saveBook(Book book);
public boolean postBook(UUID userId, BookBase bookBase);
}
