package book.store.online.repository.book.provider;

import book.store.online.model.Book;
import book.store.online.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    private static final String PROVIDER_NAME = "title";

    @Override
    public String getKey() {
        return PROVIDER_NAME;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root
                .get(PROVIDER_NAME).in(Arrays.stream(params).toArray());
    }
}
