package book.store.online.service.impl;

import book.store.online.exception.EntityNotFoundException;
import book.store.online.model.User;
import book.store.online.repository.user.UserRepository;
import book.store.online.security.JwtUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserUtil {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public User getCurrentUser(String token) {
        String email = jwtUtil.getUsername(jwtUtil.getTokenWithoutBearer(token));
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("Can't find user by email: " + email);
        }
        return optionalUser.get();
    }
}
