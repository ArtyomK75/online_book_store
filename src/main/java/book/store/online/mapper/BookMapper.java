package book.store.online.mapper;

import book.store.online.config.MapperConfig;
import book.store.online.dto.request.CreateBookRequestDto;
import book.store.online.dto.response.BookDto;
import book.store.online.dto.response.BookDtoWithoutCategoryIds;
import book.store.online.model.Book;
import book.store.online.model.Category;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
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
    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    default Book bookFromId(Long id) {
        return null;
    }
}
