package com.exchangeBook.ExchangeBook.service.impl;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSenderService {

	@Autowired
	JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String mailSender;

	@Async("sendMailExecutor")
	public void sendEmail(String toAddress, String subject, String content)
			throws UnsupportedEncodingException, MessagingException {

		String senderName = "Exchangebook.com";
		content += senderName;

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(mailSender, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);
		helper.setText(content, true);
		javaMailSender.send(message);
	}
}
