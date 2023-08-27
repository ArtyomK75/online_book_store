package book.store.online;

import book.store.online.model.Book;
import book.store.online.service.BookService;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineBookStoreApplication {
    private final BookService bookService;

    public OnlineBookStoreApplication(BookService bookService) {
        this.bookService = bookService;
    }

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Eneida");
            book.setAuthor("Kotlyarevsky");
            book.setIsbn("some ISBN");
            book.setPrice(BigDecimal.valueOf(150));
            book.setDescription("Historical book");

            bookService.save(book);
            System.out.println(bookService.findAll());
        };
    }

}
