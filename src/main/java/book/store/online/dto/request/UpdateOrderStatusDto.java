package book.store.online.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record UpdateOrderStatusDto(@NotEmpty String status) {
}
