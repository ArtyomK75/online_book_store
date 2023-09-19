package book.store.online.repository.orderitem;

import book.store.online.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findOrderItemsByOrderIdAndOrderUserId(Long id, Long userId);

    Optional<OrderItem> findOrderItemByOrderIdAndOrderUserIdAndId(
            Long orderId, Long userId, Long itemId);
}
