package com.exchangeBook.ExchangeBook.entity;

import java.time.LocalDateTime;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeForm {
	
	@EmbeddedId
	private ExchangeFormId id;
	
	@ManyToOne
	@MapsId("id_post")
	@JoinColumn(name="id_post")
	private Post post;
	
	@ManyToOne
	@MapsId("id_user")
	@JoinColumn(name="id_user")
	private User user;
	
	private String fullName;
	private String address;
	private LocalDateTime dateCreated;
}
