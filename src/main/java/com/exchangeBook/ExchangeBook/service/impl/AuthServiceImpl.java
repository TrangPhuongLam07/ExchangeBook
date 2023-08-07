package com.exchangeBook.ExchangeBook.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
import com.exchangeBook.ExchangeBook.entity.Image;
import com.exchangeBook.ExchangeBook.entity.User;
import com.exchangeBook.ExchangeBook.payload.request.LoginRequest;
import com.exchangeBook.ExchangeBook.payload.request.RegisterRequest;
import com.exchangeBook.ExchangeBook.payload.response.MessageResponse;
import com.exchangeBook.ExchangeBook.property.FileStorageProperties;
import com.exchangeBook.ExchangeBook.repository.ConfirmationTokenRepository;
import com.exchangeBook.ExchangeBook.repository.ForgetPasswordTokenRepository;
import com.exchangeBook.ExchangeBook.repository.ImageRepository;
import com.exchangeBook.ExchangeBook.repository.UserRepository;
import com.exchangeBook.ExchangeBook.security.jwt.JwtUtils;
import com.exchangeBook.ExchangeBook.security.service.UserDetailsImpl;
import com.exchangeBook.ExchangeBook.service.AuthService;
import com.exchangeBook.ExchangeBook.service.ImageService;

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
	ImageRepository imageRepository;

	@Autowired
	ImageService imageService;
	
	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	JwtUtils jwtUtils;

	private final Path rootLocation;

	public AuthServiceImpl(FileStorageProperties properties) {
		this.rootLocation = Paths.get(properties.getUploadDir());
	}

	private final String DEFAULT_AVATAR_NAME = "default_user_avatar.png";

	@Override
	public ResponseEntity<?> registerNewUser(RegisterRequest registerRequest) {

		String fileName = /* System.currentTimeMillis() + "_" + */DEFAULT_AVATAR_NAME;
		String contentType = fileName.split("[.]")[1];
		Path filePath = rootLocation.resolve(Paths.get(fileName)).normalize().toAbsolutePath();
		File file = new File(filePath.toString());
		Image avatar = imageRepository.save(Image.builder().name(fileName).contentType(contentType).size(file.length())
				.path(filePath.toString()).build());
//		imageService.uploadImage(userRequest.getAvatar());

		User user = User.builder().firstName(registerRequest.getFirstName()).lastName(registerRequest.getLastName())
				.email(registerRequest.getEmail()).password(passwordEncoder.encode(registerRequest.getPassword()))
				.role(ERole.ROLE_USER).status(EUserStatus.PENDING).point(0).avatar(avatar).build();
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

		try {
			emailSenderService.sendEmail(toAddress, subject, content);
		} catch (UnsupportedEncodingException | MessagingException e) {
			return ResponseEntity.internalServerError()
					.body(new MessageResponse("Server Error: Sending verification email has failed!"));
		}
		return ResponseEntity.ok().body(new MessageResponse("Check your email to verify!"));
	}

	@Override
	public ResponseEntity<?> verifyConfirmationToken(String confirmationToken) {
		ConfirmationToken token = confirmationTokenRepository.findByToken(confirmationToken);
		if (token != null && !token.isExpired()) {
			User user = userRepository.findById(token.getUser().getId()).get();
			user.setStatus(EUserStatus.ACTIVATED);
			userRepository.save(user);
			return ResponseEntity.ok().body(new MessageResponse("Email verified successfully!"));
		}
		return ResponseEntity.badRequest().body(new MessageResponse("Token has expired!"));
	}

	@Override
	public ResponseEntity<?> resendVerificationEmail(String email) {
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

		try {
			emailSenderService.sendEmail(toAddress, subject, content);
		} catch (UnsupportedEncodingException | MessagingException e) {
			return ResponseEntity.internalServerError()
					.body(new MessageResponse("Server Error: Resending verification email has failed!"));

		}
		return ResponseEntity.ok().body(new MessageResponse("Resend verification email successfully!"));
	}

	@Override
	public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
				.body(new MessageResponse("Login successfully!"));
	}

	@Override
	public ResponseEntity<?> sendForgetPasswordToken(String email) {
		User user = userRepository.findByEmail(email).get();
		ForgetPasswordToken token = forgetPasswordTokenRepository.findByUser(user);
		if (token != null) {
			token.renewToken();
		} else {
			token = new ForgetPasswordToken(user);
		}
//		ForgetPasswordToken token = new ForgetPasswordToken(user);
		forgetPasswordTokenRepository.save(token);

		String userFullname = user.getFirstName() + " " + user.getLastName();
		String toAddress = user.getEmail();
		String subject = "Reset Password Token!";
		String content = "Dear [[name]],<br>" + "The code below is resetting token to reset your password:<br>"
				+ "<h3>[[token]]</h3>" + "Thank you,<br>";

		content = content.replace("[[name]]", userFullname);
		content = content.replace("[[token]]", token.getToken());

		try {
			emailSenderService.sendEmail(toAddress, subject, content);
		} catch (UnsupportedEncodingException | MessagingException e) {
			return ResponseEntity.internalServerError()
					.body(new MessageResponse("Server Error: Resending verification email has failed!"));
		}

		return ResponseEntity.ok().body(new MessageResponse("Send forget password token successfully!"));
	}

	@Override
	public ResponseEntity<?> verifyResetPasswordToken(String resetPasswordToken, String newPassword) {
		ForgetPasswordToken token = forgetPasswordTokenRepository.findByToken(resetPasswordToken);
		if (token != null && !token.isExpired()) {
			User user = token.getUser();
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
			return ResponseEntity.ok().body(new MessageResponse("Reset password successfully!"));
		}
		return ResponseEntity.badRequest().body(new MessageResponse("Token has expired!"));
	}

	@Override
	public ResponseEntity<?> resendForgetPasswordToken(String email) {
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

		try {
			emailSenderService.sendEmail(toAddress, subject, content);
		} catch (UnsupportedEncodingException | MessagingException e) {
			return ResponseEntity.internalServerError()
					.body(new MessageResponse("Server Error: Resending verification email has failed!"));
		}

		return ResponseEntity.ok().body(new MessageResponse("Resend verification email successfully!"));
	}

	@Override
	public ResponseEntity<?> resetPassword(String currentPassword, String newPassword) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userEmail = "";
		if (principal.toString() != "anonymousUser") {
			userEmail = ((UserDetailsImpl) principal).getEmail();
		}
		User user = userRepository.findByEmail(userEmail).get();

		if (passwordEncoder.matches(currentPassword, user.getPassword())) {
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
			return ResponseEntity.ok().body(new MessageResponse("Reset password successfully!"));
		}
		return ResponseEntity.badRequest().body(new MessageResponse("Reset password failed!"));
	}
}
