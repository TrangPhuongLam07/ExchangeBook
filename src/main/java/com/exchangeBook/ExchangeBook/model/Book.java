package com.exchangeBook.ExchangeBook.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

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
    private static final long serialVersionUID = 1L;
    @Id
     /*Id lưu vào database là String
    vì uuid khi lưu vào database sẽ không đúng định dạng UUID*/
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id = UUID.randomUUID();
    private String name;
    private String author;
   /* @JsonFormat(pattern = "yyy-MM-dd", shape = JsonFormat.Shape.STRING)*/
    private Date createdDate = new Date(System.currentTimeMillis());
    private String description;
    private boolean isExchange;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private Set<BookImage> bookImages;


}
