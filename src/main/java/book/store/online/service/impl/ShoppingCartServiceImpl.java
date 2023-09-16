package book.store.online.service.impl;

import book.store.online.dto.request.CartItemRequestDto;
import book.store.online.dto.response.ShoppingCartDto;
import book.store.online.exception.EntityNotFoundException;
import book.store.online.mapper.CartItemMapper;
import book.store.online.mapper.ShoppingCartMapper;
import book.store.online.model.Book;
import book.store.online.model.CartItem;
import book.store.online.model.ShoppingCart;
import book.store.online.model.User;
import book.store.online.repository.cartitem.CartItemRepository;
import book.store.online.repository.shoppingcart.ShoppingCartRepository;
import book.store.online.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final UserUtil userUtil;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartDto getShoppingCart(HttpServletRequest request) {
        User user = userUtil.getCurrentUser(request);
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository
                .findShoppingCartByUserId(user.getId());
        if (optionalShoppingCart.isPresent()) {
            return shoppingCartMapper.toDto(optionalShoppingCart.get());
        }
        throw new EntityNotFoundException("Can't find shopping cart by user: " + user);
    }

    @Override
    public void addBookToCart(CartItemRequestDto cartItemRequestDto, HttpServletRequest request) {
        User user = userUtil.getCurrentUser(request);
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository
                .findShoppingCartByUserId(user.getId());
        if (optionalShoppingCart.isEmpty()) {
            throw new EntityNotFoundException("Can't find shopping cart by user: " + user);
        }
        ShoppingCart shoppingCart = optionalShoppingCart.get();
        CartItem cartItem = cartItemMapper.toEntity(cartItemRequestDto);
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.getCartItems().add(cartItemRepository.save(cartItem));
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void update(Long cartItemId, CartItemRequestDto cartDto, HttpServletRequest request) {
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId);
        if (optionalCartItem.isEmpty()) {
            throw new EntityNotFoundException("Can't find cart item by id : " + cartItemId);
        }
        CartItem cartItem = optionalCartItem.get();
        if (cartDto.getBookId() != null) {
            Book book = new Book();
            book.setId(cartDto.getBookId());
            cartItem.setBook(book);
        }
        cartItem.setQuantity(cartDto.getQuantity());
        cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteById(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
