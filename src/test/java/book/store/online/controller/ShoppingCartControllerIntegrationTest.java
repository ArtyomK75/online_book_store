package book.store.online.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import book.store.online.dto.request.CartItemRequestDto;
import book.store.online.dto.response.CartItemDto;
import book.store.online.dto.response.ShoppingCartDto;
import book.store.online.exception.EntityNotFoundException;
import book.store.online.model.CartItem;
import book.store.online.repository.cartitem.CartItemRepository;
import book.store.online.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerIntegrationTest {
    protected static MockMvc mockMvc;
    private static String token;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CartItemRepository cartItemRepository;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext,
                          @Autowired DataSource dataSource,
                          @Autowired JwtUtil jwtUtil) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/users/create_test_user.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/user_roles/create_test_user_role.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/shopping_carts/create_test_shopping_cart.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/books/create_3_test_books.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/categories/create_3_test_categories.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/book_categories/create_5_book_categories.sql"));
            token = "Bearer " + jwtUtil.generateToken("user@mail.com");
        }
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/cart_items/clear_table_cart_items.sql"));
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/users/clear_table_users.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/user_roles/clear_table_user_roles.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/shopping_carts/clear_table_shopping_carts.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/books/clear_table_books.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/categories/clear_table_categories.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/cart_items/clear_table_cart_items.sql"));
        }
    }

    @Sql(scripts = "classpath:database/cart_items/create_3_test_cart_items.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Get a shopping cart of user")
    @Test
    void getUserShoppingCart_ValidShoppingCartDto_Success() throws Exception {
        ShoppingCartDto expected = getShoppingCartDto();
        MvcResult result = mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @DisplayName("Add item to shopping cart")
    @Test
    void addBookToCart_ValidCartItem_Success() throws Exception {
        CartItemRequestDto expect = getCartItemRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(expect);

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNoContent());

        CartItem actual = getCartItemFromDB();

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(expect.getBookId(), actual.getBook().getId());
        Assertions.assertEquals(expect.getQuantity(), actual.getQuantity());
    }

    @Sql(scripts = "classpath:database/cart_items/create_1_test_cart_items.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Update cart item by id")
    @Test
    void updateCartItem_ValidCartItem_Success() throws Exception {
        CartItemRequestDto expect = getCartItemRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(expect);
        mockMvc.perform(put("/cart/cart-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNoContent());

        CartItem actual = getCartItemFromDB();

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(expect.getBookId(), actual.getBook().getId());
        Assertions.assertEquals(expect.getQuantity(), actual.getQuantity());
    }

    @Sql(scripts = "classpath:database/cart_items/create_3_test_cart_items.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Delete cart item by ID")
    @Test
    void deleteCartItem_CartItemById_Success() throws Exception {
        mockMvc.perform(delete("/cart/cart-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNoContent());
    }

    private CartItem getCartItemFromDB() throws Exception {
        List<CartItem> cartItems = cartItemRepository.findAll();
        if (cartItems.size() == 1) {
            return cartItems.get(0);
        }
        throw new EntityNotFoundException("Can't find cart item in DB");
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
