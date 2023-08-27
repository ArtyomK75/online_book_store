package book.store.online.controller;

import book.store.online.dto.request.CreateBookRequestDto;
import book.store.online.dto.response.BookDto;
import book.store.online.mapper.BookMapper;
import book.store.online.model.Book;
import book.store.online.service.BookService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping
    public List<BookDto> getAll() {
        return bookService.findAll().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        Book book = bookService.getById(id);
        return bookMapper.toDto(book);
    }

    @PostMapping
    public BookDto createBook(@RequestBody CreateBookRequestDto bookDto) {
        Book book = bookService.save(bookMapper.toModel(bookDto));
        return bookMapper.toDto(book);
    }
}
