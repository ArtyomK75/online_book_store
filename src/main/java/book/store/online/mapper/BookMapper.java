package book.store.online.mapper;

import book.store.online.config.MapperConfig;
import book.store.online.dto.request.CreateBookRequestDto;
import book.store.online.dto.response.BookDto;
import book.store.online.dto.response.BookDtoWithoutCategoryIds;
import book.store.online.model.Book;
import book.store.online.model.Category;
import book.store.online.service.BookService;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = BookService.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toEntity(CreateBookRequestDto bookDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @Mapping(target = "id", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoryIds(book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
    }

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        if (id == null) {
            return null;
        }
        Book book = new Book();
        book.setId(id);
        return book;
    }

    @Named("bookToId")
    default Long bookToId(Book book) {
        return book.getId();
    }

    @Named("bookToBookTitle")
    default String bookToBookTitle(Book book) {
        return book.getTitle();
    }

}
