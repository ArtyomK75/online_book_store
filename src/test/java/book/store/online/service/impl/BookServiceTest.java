package book.store.online.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import book.store.online.dto.request.BookSearchParametersDto;
import book.store.online.dto.request.CreateBookRequestDto;
import book.store.online.dto.response.BookDto;
import book.store.online.dto.response.BookDtoWithoutCategoryIds;
import book.store.online.mapper.BookMapper;
import book.store.online.model.Book;
import book.store.online.repository.book.BookRepository;
import book.store.online.repository.book.BookSpecificationBuilder;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @InjectMocks
    private BookServiceImpl bookService;

    @DisplayName("Save a book")
    @Test
    void saveBook_ValidBookDto_Success() {
        CreateBookRequestDto testCreateBookRequestDto = getTestCreateBookDto();
        Book testBook = getBook();
        BookDto testBookDto = getTestBookDto();
        Mockito.when(bookMapper.toModel(testCreateBookRequestDto)).thenReturn(testBook);
        Mockito.when(bookMapper.toDto(testBook)).thenReturn(testBookDto);
        Mockito.when(bookRepository.save(testBook)).thenReturn(testBook);

        BookDto actual = bookService.save(testCreateBookRequestDto);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(testBookDto, actual);
    }

    @DisplayName("Find all books")
    @Test
    void findAll_ValidListBookDto_Success() throws Exception {
        Book testBook = getBook();
        BookDto testBookDto = getTestBookDto();
        Pageable pageable = Pageable.unpaged();
        Page<Book> bookPage = new PageImpl<>(List.of(testBook));
        Mockito.when(bookMapper.toDto(testBook)).thenReturn(testBookDto);
        Mockito.when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        List<BookDto> actual = bookService.findAll(pageable);

        Assertions.assertEquals(1, actual.size());
        BookDto dto = actual.get(0);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(1L, dto.getId());
        Assertions.assertEquals("0123456789", dto.getIsbn());
    }

    @DisplayName("Get a book by ID")
    @Test
    void getById_ValidBookDtoWithoutCategoryIds_Success() {
        Optional<Book> optionalTestBook = Optional.of(getBook());
        Long testId = 1L;
        BookDtoWithoutCategoryIds testBookDto = getTestBookDtoWithoutCategoryIds();
        Mockito.when(bookMapper.toDtoWithoutCategories(optionalTestBook.get()))
                .thenReturn(testBookDto);
        Mockito.when(bookRepository.findById(testId)).thenReturn(optionalTestBook);

        BookDtoWithoutCategoryIds actual = bookService.getById(testId);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(testId, actual.getId());
        Assertions.assertEquals("0123456789", actual.getIsbn());
    }

    @DisplayName("Get a book by non existing ID")
    @Test
    void getById_WithNonExistingId_ShouldThrowException() throws Exception {
        Long testId = 100L;
        Mockito.when(bookRepository.findById(testId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                RuntimeException.class,
                () -> bookService.getById(testId)
        );
        String actual = exception.getMessage();
        String expected = "Can't find book by id: " + testId;

        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("Update a book by ID")
    @Test
    void update_ValidBookDto_Success() {
        final BookDto testBookDto = getTestBookDto();
        Long testId = 1L;
        Book testBook = getBook();
        testBook.setId(testId);
        CreateBookRequestDto testCreateBookRequestDto = getTestCreateBookDto();
        Optional<Book> optionalTestBook = Optional.of(testBook);
        Mockito.when(bookRepository.findById(testId)).thenReturn(optionalTestBook);
        Mockito.when(bookMapper.toModel(testCreateBookRequestDto)).thenReturn(testBook);
        Mockito.when(bookMapper.toDto(testBook)).thenReturn(testBookDto);
        Mockito.when(bookRepository.save(testBook)).thenReturn(testBook);

        BookDto actual = bookService.update(testId, testCreateBookRequestDto);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(testBookDto, actual);
    }

    @DisplayName("Update a book by non existing ID")
    @Test
    void update_WithNonExistingId_ShouldThrowException() throws Exception {
        CreateBookRequestDto testCreateBookRequestDto = getTestCreateBookDto();
        Long testId = 100L;
        Mockito.when(bookRepository.findById(testId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                RuntimeException.class,
                () -> bookService.update(testId, testCreateBookRequestDto)
        );
        String actual = exception.getMessage();
        String expected = "Can't find book by id: " + testId;

        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("Search a books by ID")
    @Test
    void search() {
        BookSearchParametersDto testParams = new BookSearchParametersDto(
                new String[]{"Test book"}, new String[]{"Test Author"},
                new String[]{"0123456789"}, new String[]{"Test description"});
        Book testBook = getBook();
        BookDto testBookDto = getTestBookDto();
        Specification<Book> bookSpecification = null;
        Mockito.when(bookSpecificationBuilder.build(testParams)).thenReturn(bookSpecification);
        Mockito.when(bookRepository.findAll(bookSpecification)).thenReturn(List.of(testBook));
        Mockito.when(bookMapper.toDto(testBook)).thenReturn(testBookDto);

        List<BookDto> actual = bookService.search(testParams);

        Assertions.assertEquals(1, actual.size());
        BookDto dto = actual.get(0);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(1L, dto.getId());
        Assertions.assertEquals("0123456789", dto.getIsbn());
    }

    @DisplayName("Find a books by category ID")
    @Test
    void findAllByCategoryId() {
        Book testBook = getBook();
        BookDto testBookDto = getTestBookDto();
        Long testId = 1L;
        Mockito.when(bookMapper.toDto(testBook)).thenReturn(testBookDto);
        Mockito.when(bookRepository.findAllByCategoryId(testId)).thenReturn(List.of(testBook));

        List<BookDto> actual = bookService.findAllByCategoryId(testId);

        Assertions.assertEquals(1, actual.size());
        BookDto dto = actual.get(0);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(1L, dto.getId());
        Assertions.assertEquals("0123456789", dto.getIsbn());
    }

    private CreateBookRequestDto getTestCreateBookDto() {
        return new CreateBookRequestDto()
                .setAuthor("Test Author")
                .setIsbn("0123456789")
                .setTitle("Test book")
                .setDescription("Test description")
                .setPrice(BigDecimal.valueOf(19.99))
                .setCoverImage("testImage.jpg");
    }

    private BookDto getTestBookDto() {
        return new BookDto()
                .setId(1L)
                .setAuthor("Test Author")
                .setIsbn("0123456789")
                .setTitle("Test book")
                .setDescription("Test description")
                .setPrice(BigDecimal.valueOf(19.99))
                .setCoverImage("testImage.jpg");
    }

    private BookDtoWithoutCategoryIds getTestBookDtoWithoutCategoryIds() {
        return new BookDtoWithoutCategoryIds()
                .setId(1L)
                .setAuthor("Test Author")
                .setIsbn("0123456789")
                .setTitle("Test book")
                .setDescription("Test description")
                .setPrice(BigDecimal.valueOf(19.99))
                .setCoverImage("testImage.jpg");
    }

    private Book getBook() {
        return new Book()
                .setAuthor("Test Author")
                .setIsbn("0123456789")
                .setTitle("Test book")
                .setDescription("Test description")
                .setPrice(BigDecimal.valueOf(19.99))
                .setCoverImage("testImage.jpg");
    }
}
