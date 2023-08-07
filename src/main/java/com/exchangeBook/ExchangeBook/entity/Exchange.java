package com.exchangeBook.ExchangeBook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Exchange implements Serializable {
    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id1")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "user_id2")
    private User receiver;

    private int status;

//    @ManyToOne
//    @JoinColumn(name = "book_id")
//    private Book book;

}
