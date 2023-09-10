package book.store.online.controller;

import book.store.online.dto.request.CreateCategoryRequestDto;
import book.store.online.dto.response.BookDto;
import book.store.online.dto.response.CategoryDto;
import book.store.online.service.BookService;
import book.store.online.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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

@Tag(name = "Book categories management",
        description = "Endpoints for management book categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @Operation(summary = "Create category", description = "Create a category by passed data")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoryDto createCategory(@RequestBody CreateCategoryRequestDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @Operation(summary = "Get all categories",
            description = "Get a list of all available categories")
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public List<CategoryDto> getAll() {
        return categoryService.findAll();
    }

    @Operation(summary = "Get category by ID", description = "Get a category by passed ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Operation(summary = "Update category", description = "Update a category by passed data")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @RequestBody CreateCategoryRequestDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @Operation(summary = "Delete category", description = "Delete a category by passed ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @Operation(summary = "Get books by category ID",
            description = "Get a list of books by passed category ID")
    @GetMapping("/{id}/books")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public List<BookDto> getBooksByCategoryId(@PathVariable Long id) {
        return bookService.findAllByCategoryId(id);
    }
}
