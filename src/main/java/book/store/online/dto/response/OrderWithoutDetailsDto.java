package book.store.online.dto.response;

import java.util.Set;
import lombok.Data;

@Data
public class OrderWithoutDetailsDto {
    private Set<OrderItemDto> orderItems;
}
