package book.store.online.repository;

import book.store.online.entity.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List findAll();
}
