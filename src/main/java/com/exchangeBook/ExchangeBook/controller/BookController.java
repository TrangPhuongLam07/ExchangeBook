package com.exchangeBook.ExchangeBook.controller;

import com.exchangeBook.ExchangeBook.model.Book;
import com.exchangeBook.ExchangeBook.model.BookImage;
import com.exchangeBook.ExchangeBook.modelBase.BookBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.exchangeBook.ExchangeBook.service.BookService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/book")
@CrossOrigin
public class BookController {

    @Autowired
    private BookService bookService;


    @PostMapping("/add")
    public ResponseEntity<Boolean> post(@ModelAttribute BookBase bookBase) {
        //User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID id = UUID.randomUUID();
        boolean result = bookService.postBook(id, bookBase);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getAllBook")

    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }
   /* public ResponseEntity<List<Book>> searchProducts(){
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(new ArrayList<>());
    }*/

   /* @GetMapping("/{id}")
    public Book getBookByID(@PathVariable UUID id){
        return bookService.getBookByID(id);
    }*/

}
