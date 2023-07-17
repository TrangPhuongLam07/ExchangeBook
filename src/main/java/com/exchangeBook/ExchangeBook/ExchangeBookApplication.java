package com.exchangeBook.ExchangeBook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExchangeBookApplication /*implements CommandLineRunner*/{
//	@Autowired
//	private BookRepository bookRepository;

	public static void main(String[] args) {
		SpringApplication.run(ExchangeBookApplication.class, args);
	}
//	@Bean
//	CommandLineRunner init(FileStorageService storageService) {
//		return (args) -> {
//			storageService.init();
//		};
//	}

	//Test get all books, findAll()
//	@Override
//	public void run(String... args) throws Exception {
//		Book book = new Book();
//		book.setName("book 1");
//		book.setAuthor("Logan");
//		book.setDescription("book 1 desc");
//
//		bookRepository.save(book);
//
//		Book book1 = new Book();
//		book1.setName("book 1");
//		book1.setAuthor("Logan");
//		book1.setDescription("book 1 desc");
//
//		// get or retrieve all products
//		List<Book> products = bookRepository.findAll();
//		products.forEach((p) -> {
//			System.out.println(p.getName());
//		});
//	}
}
