package com.exchangeBook.ExchangeBook.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Embeddable
@Getter
@AllArgsConstructor
@NotBlank
public class ExchangeFormId implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "id_post")
	private Long idPost;
	
	@Column(name = "id_user")
	private Long idUser;
}
