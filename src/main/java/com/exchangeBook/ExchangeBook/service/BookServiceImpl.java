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
import java.util.Optional;
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
        // var user = userRepository.findById(userId);
        if (bookBase != null) {
            book.setId(UUID.randomUUID());
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

    private boolean addImageBook(UUID bookId, List<MultipartFile> productImage) {
        //find book owner
        Book product = bookRepository.findById(bookId).get();
        if (productImage == null | product == null) {
            return false;
        }
        //create book images
        for (MultipartFile file : productImage) {
            String path = fileStorageService.saveImage(file);
            BookImage bookImage = new BookImage();
            bookImage.setBook(product);
            bookImage.setImage(path);
            bookImage.setCreatedDate(new Date(System.currentTimeMillis()));
            bookImage.setId(UUID.randomUUID());
            bookImageRepository.save(bookImage);
        }
        return true;
    }

    //Get All book functions
    @Override
    public List<Book> getAllBooks() {
        List<Book> books =bookRepository.findAll();
        return books;
    }

    @Override
    public BookBase getBookByID(UUID id) {
        Optional<Book> book = bookRepository.findById(id);
        if (!book.isPresent()){
            return null;
        }
        BookBase bookBase = BookBase.toBook(book.get());
        return bookBase;
    }

    @Override
    public List<BookBase> getAll() {
        List<Book> books = bookRepository.findAll();
        List<BookBase> bookBases = BookBase.toListBook(books);
        return bookBases;
    }
}
