package book.store.online.service;

import book.store.online.entity.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List findAll();
}
