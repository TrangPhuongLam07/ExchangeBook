package com.exchangeBook.ExchangeBook.service;

import com.exchangeBook.ExchangeBook.model.Book;
import com.exchangeBook.ExchangeBook.model.BookImage;
import com.exchangeBook.ExchangeBook.modelBase.BookBase;
import com.exchangeBook.ExchangeBook.repository.BookImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.exchangeBook.ExchangeBook.repository.BookRepository;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private BookImageRepository bookImageRepository;

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    //post book functions
    @Override
    public boolean postBook(UUID userId, BookBase bookBase) {
        Book book = new Book();
        UUID id = UUID.randomUUID();
        // var user = userRepository.findById(userId);
        if (bookBase != null) {
            //book.setId(id);
            book.setCreatedDate(new Date(System.currentTimeMillis()));
            book.setAuthor(bookBase.getAuthor());
            book.setDescription(bookBase.getDescription());
            book.setName(bookBase.getName());
            book.setExchange(false);
            //book.setUser(user.get());
            bookRepository.save(book);
            if (bookBase.getImages() != null) {
                addImageBook(book.getId(), bookBase.getImages());
            }
            return true;
        }
        return false;
    }

    private void addImageBook(UUID bookId, List<MultipartFile> productImage) {
        //find book owner
        Book product = bookRepository.findById(bookId).get();
        if (productImage == null) {
            return;
        }
        //create book images
        for (MultipartFile file : productImage) {
            String path = fileStorageService.saveImage(file);
            BookImage bookImage = new BookImage();
            bookImage.setBook(product);
            bookImage.setImage(path);
            bookImage.setCreatedDate(new Date(System.currentTimeMillis()));
            //bookImage.setId(UUID.randomUUID());
            bookImageRepository.save(bookImage);
        }
    }
//
}
