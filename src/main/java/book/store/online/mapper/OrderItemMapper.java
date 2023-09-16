package book.store.online.mapper;

import book.store.online.config.MapperConfig;
import book.store.online.dto.response.OrderItemDto;
import book.store.online.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book", qualifiedByName = "bookToId")
    OrderItemDto toDto(OrderItem orderItem);
}
