package book.store.online.dto.response;

import lombok.Data;

@Data
public class UserLoginResponseDto {
    private String token;

    public UserLoginResponseDto() {
    }

    public UserLoginResponseDto(String token) {
        this.token = token;
    }
}
