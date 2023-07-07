package com.exchangeBook.ExchangeBook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {
    @Serial
    private static final long serialVersionUID = 8908189063856649397L;
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String author;
    private Date createdDate;
    private String description;
    private boolean isExchange;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User owner;
    @OneToMany(mappedBy = "book")
    private Set<BookImage> bookImages;


}
