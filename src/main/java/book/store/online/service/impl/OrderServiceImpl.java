package book.store.online.service.impl;

import book.store.online.dto.request.PlaceOrderDto;
import book.store.online.dto.request.UpdateOrderStatusDto;
import book.store.online.dto.response.OrderDto;
import book.store.online.dto.response.OrderItemDto;
import book.store.online.dto.response.OrderWithoutDetailsDto;
import book.store.online.exception.EntityNotFoundException;
import book.store.online.mapper.OrderItemMapper;
import book.store.online.mapper.OrderMapper;
import book.store.online.model.CartItem;
import book.store.online.model.Order;
import book.store.online.model.OrderItem;
import book.store.online.model.ShoppingCart;
import book.store.online.model.User;
import book.store.online.repository.order.OrderRepository;
import book.store.online.repository.orderitem.OrderItemRepository;
import book.store.online.repository.shoppingcart.ShoppingCartRepository;
import book.store.online.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final UserUtil userUtil;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public List<OrderDto> findOrders(HttpServletRequest request) {
        User user = userUtil.getCurrentUser(request);
        return orderRepository.findOrderByUserId(user.getId()).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public void addOrder(PlaceOrderDto orderDto, HttpServletRequest request) {
        User user = userUtil.getCurrentUser(request);
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository
                .findShoppingCartByUserId(user.getId());
        if (optionalShoppingCart.isEmpty()) {
            throw new EntityNotFoundException("Can't find shopping cart by user: " + user);
        }
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING);
        order.setShippingAddress(orderDto.shippingAddress());
        order.setTotal(BigDecimal.ZERO);
        orderRepository.save(order);

        ShoppingCart shoppingCart = optionalShoppingCart.get();
        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(cartItem -> orderItemFromCartItem(order, cartItem))
                .map(orderItemRepository::save)
                .collect(Collectors.toSet());

        order.setOrderItems(orderItems);
        order.setTotal(orderItems.stream()
                .map(orderItem -> orderItem.getPrice().multiply(
                        BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        orderRepository.save(order);
    }

    @Override
    public OrderWithoutDetailsDto findListOfOrderItems(HttpServletRequest request, Long orderId) {
        User user = userUtil.getCurrentUser(request);
        Set<OrderItemDto> orderItemDtos = orderItemRepository
                .findOrderItemsByOrderIdAndOrderUserId(orderId, user.getId()).stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
        OrderWithoutDetailsDto orderWithoutDetailsDto = new OrderWithoutDetailsDto();
        orderWithoutDetailsDto.setOrderItems(orderItemDtos);
        return orderWithoutDetailsDto;
    }

    @Override
    public OrderItemDto getOrderItem(HttpServletRequest request, Long orderId, Long itemId) {
        User user = userUtil.getCurrentUser(request);
        Optional<OrderItem> optionalOrderItem = orderItemRepository
                .findOrderItemByOrderIdAndOrderUserIdAndId(orderId, user.getId(), itemId);
        if (optionalOrderItem.isEmpty()) {
            throw new EntityNotFoundException("Can't find order item by order ID: "
                    + orderId + ", by user ID :" + user.getId() + " and by item ID: " + itemId);
        }
        return orderItemMapper.toDto(optionalOrderItem.get());
    }

    @Override
    public void updateOrderStatus(Long id, UpdateOrderStatusDto orderStatusDto) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isEmpty()) {
            throw new EntityNotFoundException("Can't find order by ID: " + id);
        }
        Order order = optionalOrder.get();
        try {
            order.setStatus(Order.Status.valueOf(orderStatusDto.status()));
        } catch (NullPointerException ex) {
            throw new EntityNotFoundException("Can't find status by name: "
                    + orderStatusDto.status());
        }
        orderRepository.save(order);
    }

    private OrderItem orderItemFromCartItem(Order order, CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getBook().getPrice());
        orderItem.setOrder(order);
        return orderItem;
    }
}
