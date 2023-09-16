package book.store.online.mapper;

import book.store.online.config.MapperConfig;
import book.store.online.dto.response.ShoppingCartDto;
import book.store.online.model.ShoppingCart;
import book.store.online.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user", qualifiedByName = "userIdFromUser")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Named("userIdFromUser")
    default Long userIdFromUser(User user) {
        return user.getId();
    }
}
