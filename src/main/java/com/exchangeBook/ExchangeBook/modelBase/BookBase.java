package com.exchangeBook.ExchangeBook.modelBase;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BookBase {
    private String name;
    private String author;
    private LocalDateTime createdDate;
    private String description;
    List<MultipartFile> images;
}
