package book.store.online.repository;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getKey();

    public Specification<T> getSpecification(String[] params);
}
