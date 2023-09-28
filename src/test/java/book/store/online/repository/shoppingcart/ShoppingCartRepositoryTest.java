package book.store.online.repository.shoppingcart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import book.store.online.model.CartItem;
import book.store.online.model.ShoppingCart;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class ShoppingCartRepositoryTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Sql(scripts = {"classpath:database/books/create_3_test_books.sql",
            "classpath:database/categories/create_3_test_categories.sql",
            "classpath:database/book_categories/create_5_book_categories.sql",
            "classpath:database/users/create_test_user.sql",
            "classpath:database/user_roles/create_test_user_role.sql",
            "classpath:database/shopping_carts/create_test_shopping_cart.sql",
            "classpath:database/cart_items/create_3_test_cart_items.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/categories/clear_table_categories.sql",
            "classpath:database/books/clear_table_books.sql",
            "classpath:database/cart_items/clear_table_cart_items.sql",
            "classpath:database/users/clear_table_users.sql",
            "classpath:database/user_roles/clear_table_user_roles.sql",
            "classpath:database/shopping_carts/clear_table_shopping_carts.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

    @Test
    void findShoppingCartByUserId() {
        Long testUserId = 1L;
        Optional<ShoppingCart> optionalActual = shoppingCartRepository
                .findShoppingCartByUserId(testUserId);
        assertTrue(optionalActual.isPresent());
        ShoppingCart actual = optionalActual.get();
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(1L, actual.getUser().getId());
        Assertions.assertEquals(3, actual.getCartItems().size());
        for (CartItem cartItem: actual.getCartItems()) {
            Assertions.assertNotNull(cartItem);
            assertThat(cartItem.getId()).isIn(1L, 2L, 3L);
        }
    }
}
