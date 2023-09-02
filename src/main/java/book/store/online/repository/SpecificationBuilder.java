package book.store.online.repository;

import book.store.online.dto.request.BookSearchParametersDto;
import book.store.online.model.Book;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<Book> build(BookSearchParametersDto searchParameters);
}
