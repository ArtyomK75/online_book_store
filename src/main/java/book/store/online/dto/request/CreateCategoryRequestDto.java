package book.store.online.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record CreateCategoryRequestDto(
        @NotEmpty
        String name,
        String description) {
}
