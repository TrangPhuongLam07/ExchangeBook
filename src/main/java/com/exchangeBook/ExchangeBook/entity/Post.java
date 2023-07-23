package com.exchangeBook.ExchangeBook.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String author;

	@Column(length = 2048)
	private String description;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private EPostStatus status;

	@Column(nullable = false)
	private LocalDateTime dateCreated;

	@Column(nullable = false)
	private LocalDateTime dateUpdated;

	@Column(nullable = false)
	private LocalDateTime datePosted;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Image> images = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "id_category")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "id_user")
	private User user;

	@OneToMany(mappedBy = "post")
	private List<ExchangeForm> exchangeForms;

	public void setImages(List<Image> imageList) {
		System.out.println("list "+imageList);
		if (this.getImages() != null) {
			this.getImages().clear();
		}
		this.images = imageList;
	}
}
