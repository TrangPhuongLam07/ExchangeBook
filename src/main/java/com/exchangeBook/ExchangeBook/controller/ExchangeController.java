package com.exchangeBook.ExchangeBook.controller;

import com.exchangeBook.ExchangeBook.payload.request.EmailMessageRequest;
import com.exchangeBook.ExchangeBook.service.ExchangeBookService;
import com.exchangeBook.ExchangeBook.service.UserService;
import com.exchangeBook.ExchangeBook.service.impl.MyEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@CrossOrigin
@RestController

public class ExchangeController {
//    @Autowired
//    BookService bookService;
    @Autowired
    MyEmailService myEmailService;
    @Autowired
    ExchangeBookService exchangeBookService;
    @Autowired
    UserService userService;
//    @PostMapping
//    public ResponseEntity<?> createAnExchange(@RequestBody ExchangeRequest exchangeRequest){
//        ExchangeDto dto = exchangeBookService.exchangeBook(exchangeRequest);
//        return ResponseEntity.ok().body(dto);
//    }
    @PostMapping("/api/exchange")
    public boolean createAnExchange2(@RequestBody EmailMessageRequest emailMessageRequest) throws MessagingException, UnsupportedEncodingException {
        boolean result = exchangeBookService.exchangeBook(emailMessageRequest);
        return result;
    }
   @PostMapping("/api/update/{id}")
   public boolean updatePointAfterExchanging(@PathVariable Long id){
        boolean result = exchangeBookService.updatePoint(id);
        return result;
   }
    @GetMapping("/api/check-point")

    public boolean checkPointOfUser(){
        boolean result = userService.checkPoint();
        return result;
    }
    @GetMapping("/api/return-point")

    public int returnPointOfUser(){
        int result = userService.returnPoint();
        return result;
    }
}
