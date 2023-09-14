package book.store.online.service.impl;

import book.store.online.dto.request.BookSearchParametersDto;
import book.store.online.dto.request.CreateBookRequestDto;
import book.store.online.dto.response.BookDto;
import book.store.online.dto.response.BookDtoWithoutCategoryIds;
import book.store.online.exception.EntityNotFoundException;
import book.store.online.mapper.BookMapper;
import book.store.online.model.Book;
import book.store.online.repository.book.BookRepository;
import book.store.online.repository.book.BookSpecificationBuilder;
import book.store.online.service.BookService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDtoWithoutCategoryIds getById(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            return bookMapper.toDtoWithoutCategories(optionalBook.get());
        }
        throw new EntityNotFoundException("Can't find book by id: " + id);
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto bookDto) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = bookMapper.toModel(bookDto);
            book.setId(optionalBook.get().getId());
            return bookMapper.toDto(bookRepository.save(book));
        }
        throw new EntityNotFoundException("Can't find book by id: " + id);
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto params) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> findAllByCategoryId(Long id) {
        return bookRepository.findAllByCategoryId(id).stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }
}
