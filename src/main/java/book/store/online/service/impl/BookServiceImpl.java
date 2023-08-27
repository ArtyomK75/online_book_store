package book.store.online.service.impl;

import book.store.online.exception.EntityNotFoundException;
import book.store.online.model.Book;
import book.store.online.repository.BookRepository;
import book.store.online.service.BookService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book getById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id));
    }
}
