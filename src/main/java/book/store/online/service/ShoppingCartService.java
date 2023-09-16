package book.store.online.service;

import book.store.online.dto.request.CartItemRequestDto;
import book.store.online.dto.response.ShoppingCartDto;
import jakarta.servlet.http.HttpServletRequest;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(HttpServletRequest request);

    void addBookToCart(CartItemRequestDto cartItemRequestDto, HttpServletRequest request);

    void update(Long cartItemId, CartItemRequestDto cartDto, HttpServletRequest request);

    void deleteById(Long cartItemId);
}
