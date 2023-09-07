package book.store.online.controller;

import book.store.online.dto.request.UserLoginRequestDto;
import book.store.online.dto.request.UserRegistrationRequestDto;
import book.store.online.dto.response.UserLoginResponseDto;
import book.store.online.dto.response.UserResponseDto;
import book.store.online.exception.RegistrationException;
import book.store.online.security.AuthenticationService;
import book.store.online.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication management",
        description = "Endpoints for authentication in the book store")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Login operation",
            description = "Make login in the book store, then success return token DTO")
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }

    @Operation(summary = "Register operation",
            description = "Register new user and return accessible data")
    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }
}
