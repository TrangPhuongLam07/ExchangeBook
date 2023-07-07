package com.exchangeBook.ExchangeBook.controller;

import com.exchangeBook.ExchangeBook.model.Book;
import com.exchangeBook.ExchangeBook.model.BookImage;
import com.exchangeBook.ExchangeBook.modelBase.BookBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.exchangeBook.ExchangeBook.service.BookService;

import java.util.UUID;

@RestController
@RequestMapping("/book")
@CrossOrigin
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/add1")
    public String add(@ModelAttribute("formData") Book book){
        bookService.saveBook(book);
        return "Success";
    }
    @PostMapping("/add")
    public ResponseEntity<Boolean> add(@ModelAttribute BookBase bookBase) {
        //User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID id = UUID.randomUUID();
       // bookService.saveBook(new Book());
        boolean result = bookService.postBook(id, bookBase);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
