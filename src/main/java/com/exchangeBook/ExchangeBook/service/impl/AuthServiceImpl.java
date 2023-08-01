package com.exchangeBook.ExchangeBook.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exchangeBook.ExchangeBook.entity.ConfirmationToken;
import com.exchangeBook.ExchangeBook.entity.ERole;
import com.exchangeBook.ExchangeBook.entity.EUserStatus;
import com.exchangeBook.ExchangeBook.entity.ForgetPasswordToken;
import com.exchangeBook.ExchangeBook.entity.User;
import com.exchangeBook.ExchangeBook.payload.request.LoginRequest;
import com.exchangeBook.ExchangeBook.payload.request.RegisterRequest;
import com.exchangeBook.ExchangeBook.repository.ConfirmationTokenRepository;
import com.exchangeBook.ExchangeBook.repository.ForgetPasswordTokenRepository;
import com.exchangeBook.ExchangeBook.repository.UserRepository;
import com.exchangeBook.ExchangeBook.security.jwt.JwtUtils;
import com.exchangeBook.ExchangeBook.security.service.UserDetailsImpl;
import com.exchangeBook.ExchangeBook.service.AuthService;

import jakarta.mail.MessagingException;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	ConfirmationTokenRepository confirmationTokenRepository;

	@Autowired
	ForgetPasswordTokenRepository forgetPasswordTokenRepository;

	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	JwtUtils jwtUtils;

	@Override
	public String registerNewUser(RegisterRequest registerRequest)
			throws UnsupportedEncodingException, MessagingException {

		User user = User.builder().firstName(registerRequest.getFirstName()).lastName(registerRequest.getLastName())
				.email(registerRequest.getEmail()).password(passwordEncoder.encode(registerRequest.getPassword()))
				.role(ERole.ROLE_USER).status(EUserStatus.PENDING).point(0).build();
		userRepository.save(user);

		ConfirmationToken token = new ConfirmationToken(user);
		confirmationTokenRepository.save(token);

		String userFullname = user.getFirstName() + " " + user.getLastName();
		String toAddress = user.getEmail();
		String subject = "Complete registration!";
		String content = "Dear [[name]],<br>" + "Please click the link below to verify your email:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>";

		String url = "http://localhost:8080/api/auth/register/verify?token=" + token.getToken();
		content = content.replace("[[name]]", userFullname);
		content = content.replace("[[URL]]", url);

		emailSenderService.sendEmail(toAddress, subject, content);

		return "Check your email to verify!";
	}

	@Override
	public boolean verifyConfirmationToken(String confirmationToken) {
		ConfirmationToken token = confirmationTokenRepository.findByToken(confirmationToken);
		if (token != null && !token.isExpired()) {
			User user = userRepository.findById(token.getUser().getId()).get();
			user.setStatus(EUserStatus.ACTIVATED);
			userRepository.save(user);
			return true;
		}
		return false;
	}

	@Override
	public String resendVerificationEmail(String email) throws UnsupportedEncodingException, MessagingException {
		User user = userRepository.findByEmail(email).get();
		ConfirmationToken token = confirmationTokenRepository.findByUser(user);
		token.renewToken();
		confirmationTokenRepository.save(token);

		String userFullname = user.getFirstName() + " " + user.getLastName();
		String toAddress = user.getEmail();
		String subject = "Resend Verification Token!";
		String content = "Dear [[name]],<br>" + "Please click the link below to verify your email:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>";

		String url = "http://localhost:8080/api/auth/register/verify?token=" + token.getToken();
		content = content.replace("[[name]]", userFullname);
		content = content.replace("[[URL]]", url);

		emailSenderService.sendEmail(toAddress, subject, content);

		return "Resend verification email successfully!";
	}

	@Override
	public ResponseCookie authenticateUser(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

		return jwtCookie;
	}

	@Override
	public String sendForgetPasswordToken(String email) throws UnsupportedEncodingException, MessagingException {
		User user = userRepository.findByEmail(email).get();
		ForgetPasswordToken token = new ForgetPasswordToken(user);
		forgetPasswordTokenRepository.save(token);

		String userFullname = user.getFirstName() + " " + user.getLastName();
		String toAddress = user.getEmail();
		String subject = "Reset Password Token!";
		String content = "Dear [[name]],<br>" + "The code below is resetting token to reset your password:<br>"
				+ "<h3>[[token]]</h3>" + "Thank you,<br>";

		content = content.replace("[[name]]", userFullname);
		content = content.replace("[[token]]", token.getToken());

		emailSenderService.sendEmail(toAddress, subject, content);

		return "Send forget password token successfully!";
	}

	@Override
	public boolean verifyResetPasswordToken(String resetPasswordToken) {
		ForgetPasswordToken token = forgetPasswordTokenRepository.findByToken(resetPasswordToken);
		if (token != null && !token.isExpired()) {
			return true;
		}
		return false;
	}

	@Override
	public String resendForgetPasswordToken(String email) throws UnsupportedEncodingException, MessagingException {
		User user = userRepository.findByEmail(email).get();
		ForgetPasswordToken token = forgetPasswordTokenRepository.findByUser(user);
		token.renewToken();
		forgetPasswordTokenRepository.save(token);

		String userFullname = user.getFirstName() + " " + user.getLastName();
		String toAddress = user.getEmail();
		String subject = "Resend Resetting Password Token!";
		String content = "Dear [[name]],<br>" + "The code below is resetting token to reset your password:<br>"
				+ "<h3>[[token]]</h3>" + "Thank you,<br>";

		content = content.replace("[[name]]", userFullname);
		content = content.replace("[[token]]", token.getToken());

		emailSenderService.sendEmail(toAddress, subject, content);

		return "Resend verification email successfully!";
	}

	@Override
	public boolean resetPassword(String currentPassword, String newPassword) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userEmail = "";
		if (principal.toString() != "anonymousUser") {
			userEmail = ((UserDetailsImpl) principal).getEmail();
		}
		User user = userRepository.findByEmail(userEmail).get();

		if (passwordEncoder.matches(currentPassword, user.getPassword())) {
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
			return true;
		}
		return false;
	}
}
