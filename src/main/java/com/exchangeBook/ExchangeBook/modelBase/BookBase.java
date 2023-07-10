package com.exchangeBook.ExchangeBook.modelBase;

import com.exchangeBook.ExchangeBook.model.Book;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BookBase {
    private UUID id;
    private String name;
    private String author;
    private Date createdDate;
    private String description;
    List<MultipartFile> images;
    List<String> productImagesUrl;

    public static BookBase toBook(Book book) {
        BookBase model = new BookBase();
        model.setId(book.getId());
        model.setName(book.getName());
        model.setAuthor(book.getAuthor());
        model.setCreatedDate(book.getCreatedDate());
        model.setDescription(book.getDescription());
        model.setProductImagesUrl(book.getBookImages().stream().map(x -> x.getImage()).toList());
        return model;
    }


    public static List<BookBase> toListBook(List<Book> books) {
        return books.stream().map(BookBase::toBook).toList();
    }

}
