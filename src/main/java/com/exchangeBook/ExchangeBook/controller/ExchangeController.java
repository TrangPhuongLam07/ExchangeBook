package com.exchangeBook.ExchangeBook.controller;

import com.exchangeBook.ExchangeBook.payload.request.EmailMessageRequest;
import com.exchangeBook.ExchangeBook.service.ExchangeBookService;
import com.exchangeBook.ExchangeBook.service.UserService;
import com.exchangeBook.ExchangeBook.service.impl.MyEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/exchange")
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
    @PostMapping("")
    public boolean createAnExchange2(@RequestBody EmailMessageRequest emailMessageRequest){
        boolean result = exchangeBookService.exchangeBook(emailMessageRequest);
        return result;
    }
   @PostMapping()
   public boolean updatePointAfterExchanging(@PathVariable Long id){
        boolean result = exchangeBookService.updatePoint(id);
        return result;
   }
    @GetMapping("/check-point/{id}")

    public boolean checkPointOfUser(@PathVariable Long id){
        boolean result = userService.checkPoint(id);
        return result;
    }
}
