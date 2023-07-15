package com.exchangeBook.ExchangeBook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "book_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookImage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
     /*Id lưu vào database là String
    vì uuid khi lưu vào database sẽ không đúng định dạng UUID*/
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id=UUID.randomUUID();
    private Date createdDate;
    private String image;
    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;

}
