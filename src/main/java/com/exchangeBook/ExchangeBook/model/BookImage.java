package com.exchangeBook.ExchangeBook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "book_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookImage implements Serializable {
    @Serial
    private static final long serialVersionUID = -4947877913705464629L;
    @Id
    @GeneratedValue
    private UUID id;
    private LocalDateTime createdDate;
    private String image;
    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;

}
