package book.store.online.service;

import book.store.online.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List findAll();
}
