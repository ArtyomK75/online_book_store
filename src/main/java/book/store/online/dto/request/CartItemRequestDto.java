package book.store.online.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class CartItemRequestDto {
    private Long bookId;
    private int quantity;
}
