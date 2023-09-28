package book.store.online.service.impl;

import static org.mockito.ArgumentMatchers.any;

import book.store.online.dto.request.CartItemRequestDto;
import book.store.online.dto.response.CartItemDto;
import book.store.online.dto.response.ShoppingCartDto;
import book.store.online.mapper.CartItemMapper;
import book.store.online.mapper.ShoppingCartMapper;
import book.store.online.model.CartItem;
import book.store.online.model.Role;
import book.store.online.model.ShoppingCart;
import book.store.online.model.User;
import book.store.online.repository.cartitem.CartItemRepository;
import book.store.online.repository.shoppingcart.ShoppingCartRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {
    private static final String token = "test token";
    @Mock
    private UserUtil userUtil;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private CartItemRepository cartItemRepository;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @DisplayName("Get a shopping cart of user")
    @Test
    void getShoppingCart_ValidShoppingCartDto_Success() {
        User user = getUser();
        ShoppingCartDto expected = getShoppingCartDto();
        ShoppingCart shoppingCart = new ShoppingCart()
                .setId(1L)
                .setUser(user);
        Mockito.when(userUtil.getCurrentUser(token)).thenReturn(user);
        Mockito.when(shoppingCartRepository.findShoppingCartByUserId(any()))
                .thenReturn(Optional.of(shoppingCart));
        Mockito.when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);

        ShoppingCartDto actual = shoppingCartService.getShoppingCart(token);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @DisplayName("Add a book to cart")
    @Test
    void addBookToCart_SaveNewCartItem_Success() {
        User user = getUser();
        ShoppingCart shoppingCart = new ShoppingCart()
                .setId(1L)
                .setUser(user)
                .setCartItems(new HashSet<>());
        CartItem cartItem = new CartItem();
        CartItemRequestDto cartItemRequestDto = getCartItemRequestDto();
        Mockito.when(userUtil.getCurrentUser(token)).thenReturn(user);
        Mockito.when(shoppingCartRepository.findShoppingCartByUserId(any()))
                .thenReturn(Optional.of(shoppingCart));
        Mockito.when(cartItemMapper.toEntity(cartItemRequestDto)).thenReturn(cartItem);
        Mockito.when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        Mockito.when(shoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart);

        shoppingCartService.addBookToCart(cartItemRequestDto, token);
    }

    @DisplayName("Update a book to cart")
    @Test
    void update_ExistingCartItem_Success() {
        User user = getUser();
        Mockito.when(userUtil.getCurrentUser(token)).thenReturn(user);
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        Mockito.when(cartItemRepository
                        .findByIdAndShoppingCartUserId(cartItem.getId(),user.getId()))
                .thenReturn(Optional.of(cartItem));
        Mockito.when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        CartItemRequestDto cartItemRequestDto = getCartItemRequestDto();

        shoppingCartService.update(1L, cartItemRequestDto, token);
    }

    private User getUser() {
        return new User()
                .setId(1L)
                .setEmail("user@mail.com")
                .setFirstName("user")
                .setLastName("usersky")
                .setEmail("user@mail.com")
                .setRoles(Set.of(new Role(Role.RoleName.ROLE_USER)))
                .setPassword("01234")
                .setShippingAddress("test address");
    }

    private CartItemRequestDto getCartItemRequestDto() {
        return new CartItemRequestDto()
                .setBookId(3L)
                .setQuantity(20);
    }

    private ShoppingCartDto getShoppingCartDto() {
        return new ShoppingCartDto()
                .setId(1L)
                .setUserId(1L)
                .setCartItems(getSetTestCartItems());
    }

    private Set<CartItemDto> getSetTestCartItems() {
        Set<CartItemDto> cartItemDtoSet = new HashSet<>();
        cartItemDtoSet.add(new CartItemDto()
                .setId(1L)
                .setBookId(1L)
                .setBookTitle("Test Title")
                .setQuantity(5));

        cartItemDtoSet.add(new CartItemDto()
                .setId(2L)
                .setBookId(3L)
                .setBookTitle("Test Title 2")
                .setQuantity(3));

        cartItemDtoSet.add(new CartItemDto()
                .setId(3L)
                .setBookId(2L)
                .setBookTitle("Test Title 1")
                .setQuantity(2));
        return cartItemDtoSet;
    }
}
