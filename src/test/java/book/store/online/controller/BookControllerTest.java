package book.store.online.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import book.store.online.dto.request.CreateBookRequestDto;
import book.store.online.dto.response.BookDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
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
class BookControllerIntegrationTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
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
                    new ClassPathResource("database/books/clear_table_books.sql"));
        }
    }

    @Sql(scripts = "classpath:database/books/create_3_test_books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Get all available books")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void getAll_ListValidBookDto_Success() throws Exception {
        MvcResult result = mockMvc.perform(get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> actual = List.of(objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto[].class));

        Assertions.assertEquals(3, actual.size());
        for (BookDto dto: actual) {
            Assertions.assertNotNull(dto);
            assertThat(dto.getId()).isIn(1L, 2L, 3L);
            assertThat(dto.getIsbn()).isIn("01234567891", "01234567892", "01234567893");
        }

    }

    @Sql(scripts = "classpath:database/books/create_test_book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Get book by ID")
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getBookById_GetValidBookDto_Success() throws Exception {
        BookDto expected = getTestBookDto();
        MvcResult result = mockMvc.perform(get("/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @DisplayName("Create new book")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto createDto = getTestCreateBookDto();

        BookDto expected = getTestBookDto();

        String jsonRequest = objectMapper.writeValueAsString(createDto);

        MvcResult result = mockMvc.perform(post("/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Update existing book")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto createDto = getTestCreateBookDto();

        String jsonRequest = objectMapper.writeValueAsString(createDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        Long id = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class).getId();

        createDto.setAuthor("Test Author Changed")
                .setPrice(BigDecimal.valueOf(55.55));

        final BookDto expected = new BookDto()
                .setAuthor(createDto.getAuthor())
                .setIsbn(createDto.getIsbn())
                .setTitle(createDto.getTitle())
                .setDescription(createDto.getDescription())
                .setPrice(createDto.getPrice())
                .setCoverImage(createDto.getCoverImage());

        jsonRequest = objectMapper.writeValueAsString(createDto);

        result = mockMvc.perform(put("/books/" + id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Sql(scripts = "classpath:database/books/create_3_test_books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Delete book by ID")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteBook_BookDeletedById_Success() throws Exception {
        mockMvc.perform(delete("/books/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Sql(scripts = "classpath:database/books/create_3_test_books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Search book by ID")
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void searchBooks() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/search?isbn=01234567891,01234567892")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> actual = List.of(objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto[].class));

        Assertions.assertEquals(2, actual.size());
        for (BookDto dto: actual) {
            Assertions.assertNotNull(dto);
            assertThat(dto.getId()).isIn(1L, 2L);
            assertThat(dto.getIsbn()).isIn("01234567891", "01234567892");
        }
    }

    private CreateBookRequestDto getTestCreateBookDto() {
        return new CreateBookRequestDto()
                .setAuthor("Test Author")
                .setIsbn("0123456789")
                .setTitle("Test book")
                .setDescription("Test description")
                .setPrice(BigDecimal.valueOf(19.99))
                .setCoverImage("testImage.jpg");
    }

    private BookDto getTestBookDto() {
        return new BookDto()
                .setId(1L)
                .setAuthor("Test Author")
                .setIsbn("0123456789")
                .setTitle("Test book")
                .setDescription("Test description")
                .setPrice(BigDecimal.valueOf(19.99))
                .setCoverImage("testImage.jpg");
    }
}
