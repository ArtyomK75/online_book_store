package book.store.online.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import book.store.online.dto.request.CreateCategoryRequestDto;
import book.store.online.dto.response.BookDto;
import book.store.online.dto.response.CategoryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/categories/clear_table_categories.sql"));
        }
    }

    @Test
    @DisplayName("Create new category")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createCategory_ValidRequestDto_Success() throws Exception {
        CreateCategoryRequestDto categoryRequestDto =
                new CreateCategoryRequestDto("Category 1", "Description 1");

        CategoryDto expected = new CategoryDto()
                .setDescription(categoryRequestDto.description())
                .setName(categoryRequestDto.name());

        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Sql(scripts = "classpath:database/categories/create_3_test_categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Get all available categories")
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAll_ListValidCategoryDto_Success() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<CategoryDto> actual = List.of(objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto[].class));

        Assertions.assertEquals(3, actual.size());
        for (CategoryDto dto: actual) {
            Assertions.assertNotNull(dto);
            assertThat(dto.getId()).isIn(1L, 2L, 3L);
            assertThat(dto.getName()).isIn("Test Category", "Test Category 1", "Test Category 2");
        }

    }

    @Sql(scripts = "classpath:database/categories/create_3_test_categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Get category by ID")
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getCategoryById_getValidResponseDto_Success() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(2L, actual.getId());
        Assertions.assertEquals("Test Category 1", actual.getName());
        Assertions.assertEquals("Test description  1", actual.getDescription());
    }

    @Sql(scripts = "classpath:database/categories/create_3_test_categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Update category by ID")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateCategory_ValidUpdatedCategoryDto_Success() throws Exception {
        CreateCategoryRequestDto categoryRequestDto =
                new CreateCategoryRequestDto("Category 1 cheng", "Description 1 cheng");

        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);

        MvcResult result = mockMvc.perform(put("/categories/2")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);

        CategoryDto expected = new CategoryDto()
                .setId(2L)
                .setDescription(categoryRequestDto.description())
                .setName(categoryRequestDto.name());

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Sql(scripts = "classpath:database/categories/create_3_test_categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Delete category by ID")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteCategory_categoryDeletedById_Success() throws Exception {
        mockMvc.perform(delete("/categories/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Sql(scripts = {"classpath:database/books/create_3_test_books.sql",
            "classpath:database/categories/create_3_test_categories.sql",
            "classpath:database/book_categories/create_5_book_categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Get books by category ID")
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getBooksByCategoryId_ValidResponseListDto_Success() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/3/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> actual = List.of(objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto[].class));

        Assertions.assertEquals(2, actual.size());
        for (BookDto dto: actual) {
            Assertions.assertNotNull(dto);
            assertThat(dto.getId()).isIn(2L, 3L);
        }
    }
}
