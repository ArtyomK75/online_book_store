package book.store.online.service;

import book.store.online.dto.request.BookSearchParametersDto;
import book.store.online.dto.request.CreateBookRequestDto;
import book.store.online.dto.response.BookDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto getById(Long id);

    BookDto update(Long id, CreateBookRequestDto bookDto);

    void delete(Long id);

    List<BookDto> search(BookSearchParametersDto searchParameters);
}
