package book.store.online.service;

import book.store.online.dto.request.CartItemRequestDto;
import book.store.online.dto.response.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(String token);

    void addBookToCart(CartItemRequestDto cartItemRequestDto, String token);

    void update(Long cartItemId, CartItemRequestDto cartDto, String token);

    void deleteById(Long cartItemId);
}
