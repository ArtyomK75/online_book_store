package book.store.online.dto.request;

import book.store.online.lib.FieldsValueMatch;
import book.store.online.lib.ValidEmail;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@FieldsValueMatch(
        field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords do not match!"
)
@Data
public class UserRegistrationRequestDto {
    @ValidEmail
    private String email;
    @NotEmpty
    @Size(min = 4, max = 40)
    private String password;
    private String repeatPassword;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    private String shippingAddress;
}
