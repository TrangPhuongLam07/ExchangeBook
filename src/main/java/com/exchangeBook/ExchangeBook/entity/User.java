package com.exchangeBook.ExchangeBook.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Column(length = 50)
	private String firstName;
	
	@Column(length = 50)
	private String lastName;
	
	@Column(length = 10)
	private String phoneNumber;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ERole role;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private EUserStatus status;
	
	@Column(nullable = false)
	private int point;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Image image;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Address address;
	
	@OneToMany(mappedBy = "user")
	private List<Post> posts;
	
	@OneToMany(mappedBy = "user")
	private List<ExchangeForm> exchangeForms;
}
