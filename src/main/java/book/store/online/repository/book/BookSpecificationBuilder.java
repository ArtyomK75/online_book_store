package book.store.online.repository.book;

import book.store.online.dto.request.BookSearchParametersDto;
import book.store.online.model.Book;
import book.store.online.repository.SpecificationBuilder;
import book.store.online.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String PROVIDER_NAME_TITLE = "title";
    private static final String PROVIDER_NAME_AUTHOR = "author";
    private static final String PROVIDER_NAME_ISBN = "isbn";
    private static final String PROVIDER_NAME_DESCRIPTION = "description";
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.title() != null && searchParameters.title().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(PROVIDER_NAME_TITLE)
                    .getSpecification(searchParameters.title()));
        }
        if (searchParameters.author() != null && searchParameters.author().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(PROVIDER_NAME_AUTHOR)
                    .getSpecification(searchParameters.author()));
        }
        if (searchParameters.isbn() != null && searchParameters.isbn().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(PROVIDER_NAME_ISBN)
                    .getSpecification(searchParameters.isbn()));
        }
        if (searchParameters.description() != null && searchParameters.description().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(PROVIDER_NAME_DESCRIPTION)
                    .getSpecification(searchParameters.description()));
        }
        return spec;
    }
}
