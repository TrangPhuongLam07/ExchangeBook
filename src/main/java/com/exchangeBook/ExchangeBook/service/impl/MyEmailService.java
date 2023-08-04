package com.exchangeBook.ExchangeBook.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MyEmailService {

    @Autowired
    MailSender mailSender;
    public void sendEmail(String to, String title, String text){
//        SimpleEmailMessage message = new SimpleEmailMessage();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);

//        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject(title);
        mailMessage.setText(text);
        mailSender.send(mailMessage);
//        javaMailSender.send(message);
    }
}
