package book.store.online.service.impl;

import book.store.online.dto.request.UserLoginRequestDto;
import book.store.online.dto.request.UserRegistrationRequestDto;
import book.store.online.dto.response.UserLoginResponseDto;
import book.store.online.dto.response.UserResponseDto;
import book.store.online.exception.RegistrationException;
import book.store.online.mapper.UserMapper;
import book.store.online.model.Role;
import book.store.online.model.ShoppingCart;
import book.store.online.model.User;
import book.store.online.repository.shoppingcart.ShoppingCartRepository;
import book.store.online.repository.user.UserRepository;
import book.store.online.service.RoleService;
import book.store.online.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Role.RoleName DEFAULT_ROLE = Role.RoleName.ROLE_USER;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findUserByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Unable to complete registration");
        }
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setShippingAddress(requestDto.getShippingAddress());
        Role defaultRole = roleService.getByName(DEFAULT_ROLE.name());
        user.setRoles(Set.of(defaultRole));
        user = userRepository.save(user);

        ShoppingCart userShoppingCart = new ShoppingCart();
        userShoppingCart.setUser(user);
        shoppingCartRepository.save(userShoppingCart);

        return userMapper.toDto(user);
    }

    @Override
    public UserLoginResponseDto login(UserLoginRequestDto requestDto) {
        return null;
    }
}
