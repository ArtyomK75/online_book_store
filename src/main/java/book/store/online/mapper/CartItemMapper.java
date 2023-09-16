package book.store.online.mapper;

import book.store.online.config.MapperConfig;
import book.store.online.dto.request.CartItemRequestDto;
import book.store.online.dto.response.CartItemDto;
import book.store.online.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book", qualifiedByName = "bookToId")
    @Mapping(target = "bookTitle", source = "book", qualifiedByName = "bookToBookTitle")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toEntity(CartItemRequestDto cartItemDto);
}
