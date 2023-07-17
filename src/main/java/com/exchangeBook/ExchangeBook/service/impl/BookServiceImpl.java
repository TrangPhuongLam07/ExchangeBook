package com.exchangeBook.ExchangeBook.service.impl;

//@Service
//public class BookServiceImpl /*implements BookService*/ {
//    @Autowired
//    private BookRepository bookRepository;
//
//    @Autowired
//    private FileStorageService fileStorageService;
//
//    @Autowired
//    private BookImageRepository bookImageRepository;

//    @Override
//    public Book saveBook(Book book) {
//        return bookRepository.save(book);
//    }

    //post book functions
//    @Override
//    public boolean postBook(UUID userId, BookBase bookBase) {
//        Book book = new Book();
//        UUID id = UUID.randomUUID();
//        // var user = userRepository.findById(userId);
//        if (bookBase != null) {
//            //book.setId(id);
//            book.setCreatedDate(new Date(System.currentTimeMillis()));
//            book.setAuthor(bookBase.getAuthor());
//            book.setDescription(bookBase.getDescription());
//            book.setName(bookBase.getName());
//            book.setExchange(false);
//            //book.setUser(user.get());
//            bookRepository.save(book);
//            if (bookBase.getImages() != null) {
//                addImageBook(book.getId(), bookBase.getImages());
//            }
//            return true;
//        }
//        return false;
//    }

//    private void addImageBook(UUID bookId, List<MultipartFile> productImage) {
//        //find book owner
//        Book product = bookRepository.findById(bookId).get();
//        if (productImage == null) {
//            return;
//        }
//        //create book images
//        for (MultipartFile file : productImage) {
//            String path = fileStorageService.saveImage(file);
//            BookImage bookImage = new BookImage();
//            bookImage.setBook(product);
//            bookImage.setImage(path);
//            bookImage.setCreatedDate(new Date(System.currentTimeMillis()));
//            //bookImage.setId(UUID.randomUUID());
//            bookImageRepository.save(bookImage);
//        }
//    }

    //Get All book functions
//    @Override
//    public List<Book> getAllBooks() {
//        List<Book> books =bookRepository.findAll();
//        return books;
//    }
//
//    @Override
//    public Optional<Book> getBookByID(UUID id) {
//        return bookRepository.findById(id);
//    }
//}
