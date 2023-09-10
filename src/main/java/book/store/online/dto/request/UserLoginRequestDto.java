package book.store.online.dto.request;

import book.store.online.lib.ValidEmail;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotEmpty
        @Size(min = 8, max = 20)
        @ValidEmail
        String email,
        @NotEmpty
        @Size(min = 4, max = 20)
        String password) {
}
