package book.store.online.controller;

import book.store.online.dto.request.PlaceOrderDto;
import book.store.online.dto.request.UpdateOrderStatusDto;
import book.store.online.dto.response.OrderDto;
import book.store.online.dto.response.OrderItemDto;
import book.store.online.dto.response.OrderWithoutDetailsDto;
import book.store.online.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management",
        description = "Endpoints for management users orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Get orders data",
            description = "Get all order by passed token for user")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<OrderDto> getUserOrders(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return orderService.findOrders(token);
    }

    @Operation(summary = "Place order",
            description = "Place order based on passed token and data taken from cart")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addBookToCart(
            @RequestBody @Valid PlaceOrderDto orderDto,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        orderService.addOrder(orderDto, token);
    }

    @Operation(summary = "Get order data",
            description = "Get order by passed token for user and order's ID")
    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('ROLE_USER')")
    public OrderWithoutDetailsDto getUserOrder(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable Long orderId) {
        return orderService.findListOfOrderItems(token, orderId);
    }

    @Operation(summary = "Get order item data",
            description = "Get order item data by passed token for user, order's ID and item ID")
    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public OrderItemDto getUserOrderItem(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.getOrderItem(token, orderId, itemId);
    }

    @Operation(summary = "Update status of an order",
            description = "Update status of an order passed on order's ID")
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrderStatus(@PathVariable Long id,
                                  @RequestBody @Valid UpdateOrderStatusDto orderStatusDto) {
        orderService.updateOrderStatus(id, orderStatusDto);
    }
}
