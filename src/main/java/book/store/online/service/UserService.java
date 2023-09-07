package book.store.online.service;

import book.store.online.dto.request.UserLoginRequestDto;
import book.store.online.dto.request.UserRegistrationRequestDto;
import book.store.online.dto.response.UserLoginResponseDto;
import book.store.online.dto.response.UserResponseDto;
import book.store.online.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;

    UserLoginResponseDto login(UserLoginRequestDto requestDto);
}
