package book.store.online.repository.book;

import static org.assertj.core.api.Assertions.assertThat;

import book.store.online.model.Book;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Sql(scripts = {"classpath:database/books/create_3_test_books.sql",
            "classpath:database/categories/create_3_test_categories.sql",
            "classpath:database/book_categories/create_5_book_categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/categories/clear_table_categories.sql",
            "classpath:database/books/clear_table_books.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Find all books by Category ID")
    @Test
    void findAllByCategoryId_CategoryPresentInDataBase_Success() {
        Long testCategoryId = 3L;

        List<Book> actual = bookRepository.findAllByCategoryId(testCategoryId);

        Assertions.assertEquals(2, actual.size());
        for (Book book: actual) {
            Assertions.assertNotNull(book);
            assertThat(book.getId()).isIn(2L, 3L);
        }
    }
}
