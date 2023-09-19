package book.store.online.mapper;

import book.store.online.config.MapperConfig;
import book.store.online.dto.response.OrderDto;
import book.store.online.dto.response.OrderWithoutDetailsDto;
import book.store.online.model.Order;
import book.store.online.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "status", source = "status", qualifiedByName = "statusFromOrder")
    @Mapping(target = "userId", source = "user", qualifiedByName = "userIdFromUser")
    OrderDto toDto(Order order);

    OrderWithoutDetailsDto toDtoWithoutDetails(Order order);

    @Named("statusFromOrder")
    default String statusFromOrder(Order.Status status) {
        return status.toString();
    }

    @Named("userIdFromUser")
    default Long userIdFromUser(User user) {
        return user.getId();
    }
}
