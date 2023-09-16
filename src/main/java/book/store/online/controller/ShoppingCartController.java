package book.store.online.controller;

import book.store.online.dto.request.CartItemRequestDto;
import book.store.online.dto.response.ShoppingCartDto;
import book.store.online.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management",
        description = "Endpoints for management users shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get shopping cart data",
            description = "Get shopping cart by passed token")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCartDto getUserShoppingCart(HttpServletRequest request) {
        return shoppingCartService.getShoppingCart(request);
    }

    @Operation(summary = "Add data to shopping cart",
            description = "Add books to shopping cart by passed token")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addBookToCart(@RequestBody CartItemRequestDto cartDto,
            HttpServletRequest request) {
        shoppingCartService.addBookToCart(cartDto, request);
    }

    @Operation(summary = "Update cart item",
            description = "Update a cart item by passed data")
    @PutMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCartItem(@PathVariable Long cartItemId,
                                  @RequestBody CartItemRequestDto cartDto,
                                  HttpServletRequest request) {
        shoppingCartService.update(cartItemId, cartDto, request);
    }

    @Operation(summary = "Delete cart item",
            description = "Delete a cart item by passed ID")
    @DeleteMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCartItem(@PathVariable Long cartItemId) {
        shoppingCartService.deleteById(cartItemId);
    }
}
