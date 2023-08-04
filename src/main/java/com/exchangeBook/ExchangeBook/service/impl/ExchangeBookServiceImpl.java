package com.exchangeBook.ExchangeBook.service.impl;


import com.exchangeBook.ExchangeBook.entity.Exchange;
import com.exchangeBook.ExchangeBook.entity.ExchangeForm;
import com.exchangeBook.ExchangeBook.entity.Post;
import com.exchangeBook.ExchangeBook.entity.User;

import com.exchangeBook.ExchangeBook.payload.request.EmailMessageRequest;
import com.exchangeBook.ExchangeBook.repository.ExchangeRepository;
import com.exchangeBook.ExchangeBook.repository.PostRepository;
import com.exchangeBook.ExchangeBook.repository.UserRepository;
import com.exchangeBook.ExchangeBook.security.service.UserDetailsImpl;
import com.exchangeBook.ExchangeBook.service.ExchangeBookService;
import com.exchangeBook.ExchangeBook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ExchangeBookServiceImpl implements ExchangeBookService {
    @Autowired
    private ExchangeRepository exchangeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
//    @Autowired
//    private BookRepository bookRepository;
//    @Autowired
//    ExchangeMapper exchangeMapper;
    private UserService userService = new UserServiceImpl();
//    private MyEmailService myEmailService = new MyEmailService();

    @Autowired
    MailSender mailSender;
    private MyEmailService emailService = new MyEmailService();

//    @Override
//    public Book exchangeBook(Long userRequestId, Book book) {
//        boolean exchangeRequest = userService.requestBook(userRequestId);
//        User userRequest = userRepository.findById(userRequestId).orElse(null);
//        if (exchangeRequest) {
//            Exchange borrowerBook = new Exchange();
////            borrowerBook.setId(UUID.randomUUID());
//            borrowerBook.setUser(userService.getUserById(userRequestId));
//            borrowerBook.setBook(book);
//            borrowerBook.setStatus(0);
//            exchangeRepository.save(borrowerBook);
////          send to  email
////            myEmailService.sendEmail(book.getOwner().getEmail(),"","");
////            emailService.sendEmail(userRequest.getEmail(), book.getOwner().getEmail(), "Trao doi sach", "Toi muon nhan sach");
//        }
//        return book;
//    }

//    @Override
//    public ExchangeDto exchangeBook(ExchangeRequest exchangeRequest) {
//        User userRequest = userRepository.findById(exchangeRequest.getUserId()).orElse(null);
//        Exchange exchange = new Exchange();
//        Book book =bookRepository.findById(exchangeRequest.getBookId()).orElse(null);
//        if (userService.requestBook(userRequest.getId())) {
////            exchange.setId(UUID.randomUUID());
//            exchange.setUser(userRequest);
//            exchange.setBook(book);
//            exchange.setStatus(0);
//            exchangeRepository.save(exchange);
//        }
////        emailService.sendEmail(userRequest.getEmail(), book.getOwner().getEmail(), "Trao doi sach", "Toi muon nhan sach");
//        ExchangeDto dto = exchangeMapper.toExchangeDto(exchange);
//        return dto;
//    }



    @Override
    public boolean exchangeBook(EmailMessageRequest emailMessageRequest) {
//        User userRequest = userRepository.findById(exchangeRequest2.getUserId()).orElse(null);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = "";
        if (principal.toString() != "anonymousUser") {
            userEmail = ((UserDetailsImpl) principal).getEmail();
        }
        User receiver = userRepository.findByEmail(userEmail).get();
        Post post = postRepository.findById(emailMessageRequest.getIdPost()).get();
        User sender = post.getUser();
        Exchange exchange = new Exchange();
        if( receiver!=null && receiver.getPoint()>0 ){
            exchange.setReceiver(receiver);
            exchange.setSender(sender);
            exchange.setStatus(0);
            exchangeRepository.save(exchange);
            emailService.sendEmail(sender.getEmail(), emailMessageRequest.getTitle(),"Mail người muốn nhận sách: "+receiver.getEmail()+"\n"+emailMessageRequest.getContent() );
            return true;
        }
        return false;
    }

    @Override
    public boolean updatePoint(Long id) {
        Exchange exchange = exchangeRepository.findById(id).get();
        if(exchange!=null){
        User sender = exchange.getSender();
        User receiver = exchange.getReceiver();
        sender.setPoint(sender.getPoint()+1);
        receiver.setPoint(receiver.getPoint()-1);
        exchange.setStatus(1);
        exchangeRepository.save(exchange);
        userRepository.save(sender);
        userRepository.save(receiver);
        return true;
        }
        return false;
    }
}
