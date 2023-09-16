package book.store.online.service;

import book.store.online.dto.request.PlaceOrderDto;
import book.store.online.dto.request.UpdateOrderStatusDto;
import book.store.online.dto.response.OrderDto;
import book.store.online.dto.response.OrderItemDto;
import book.store.online.dto.response.OrderWithoutDetailsDto;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderService {
    List<OrderDto> findOrders(HttpServletRequest request);

    void addOrder(PlaceOrderDto orderDto, HttpServletRequest request);

    OrderWithoutDetailsDto findListOfOrderItems(HttpServletRequest request, Long orderId);

    OrderItemDto getOrderItem(HttpServletRequest request, Long orderId, Long itemId);

    void updateOrderStatus(Long id, UpdateOrderStatusDto orderStatusDto);
}
