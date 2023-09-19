package book.store.online.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record PlaceOrderDto(@NotEmpty String shippingAddress) {
}
