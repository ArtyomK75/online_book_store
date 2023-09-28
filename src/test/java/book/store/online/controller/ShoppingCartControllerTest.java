package book.store.online.controller;

import book.store.online.dto.response.BookDto;
import book.store.online.dto.response.CartItemDto;
import book.store.online.dto.response.CategoryDto;
import book.store.online.dto.response.ShoppingCartDto;
import book.store.online.model.Role;
import book.store.online.model.User;
import book.store.online.security.JwtUtil;
import book.store.online.service.ShoppingCartService;
import book.store.online.service.impl.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerIntegrationTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext,
                          @Autowired DataSource dataSource) throws SQLException {
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
        }
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/shopping_carts/clear_table_shopping_carts.sql"));
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
                    new ClassPathResource("database/shopping_carts/clear_table_shopping_carts.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/books/clear_table_books.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/categories/clear_table_categories.sql"));
        }
    }

    @Sql(scripts = "classpath:database/cart_items/create_3_test_cart_items.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Get a shopping cart of user")
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void getUserShoppingCart() throws Exception {
        ShoppingCartDto expected = getShoppingCartDto();
        String token = "Bearer Some test token";

        //token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBtYWlsLmNvbSIsImlhdCI6MTY5NTEyOTgzNywiZXhwIjoxNjk1MTMyODM3fQ.73JOe-5t3ssyQhXSRq_uKFecfYgHHpRXuuwC1tnEmH8";
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

    @Test
    void addBookToCart() {
    }

    @Test
    void updateCartItem() {
    }

    @Test
    void deleteCartItem() {
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

}