package book.store.online.service;

import book.store.online.dto.request.PlaceOrderDto;
import book.store.online.dto.request.UpdateOrderStatusDto;
import book.store.online.dto.response.OrderDto;
import book.store.online.dto.response.OrderItemDto;
import book.store.online.dto.response.OrderWithoutDetailsDto;
import java.util.List;

public interface OrderService {
    List<OrderDto> findOrders(String token);

    void addOrder(PlaceOrderDto orderDto, String token);

    OrderWithoutDetailsDto findListOfOrderItems(String token, Long orderId);

    OrderItemDto getOrderItem(String token, Long orderId, Long itemId);

    void updateOrderStatus(Long id, UpdateOrderStatusDto orderStatusDto);
}
