package com.exchangeBook.ExchangeBook.entity;

import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForgetPasswordToken {

	@Transient
	private final long EXPIRED_TIME_SECONDS = 300;
	
	public ForgetPasswordToken(User user) {
		this.user = user;
		this.token = RandomStringUtils.randomAlphanumeric(6);
		this.createAt = LocalDateTime.now();
		this.expireAt = createAt.plusSeconds(EXPIRED_TIME_SECONDS);
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String token;
	
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createAt;
	
	private LocalDateTime expireAt;
	
	@OneToOne
	@JoinColumn(name = "id_user")
	private User user;
	
	@Transient
	private boolean isExpired;
	
	public boolean isExpired() {
		return expireAt.isBefore(LocalDateTime.now());
	}
	
	public void renewToken() {
		this.token =  RandomStringUtils.randomAlphanumeric(6);
		this.createAt = LocalDateTime.now();
		this.expireAt = createAt.plusSeconds(EXPIRED_TIME_SECONDS);
	}
}
