package book.store.online.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import book.store.online.dto.request.CreateCategoryRequestDto;
import book.store.online.dto.response.CategoryDto;
import book.store.online.mapper.CategoryMapper;
import book.store.online.model.Category;
import book.store.online.repository.category.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @DisplayName("Find all categories")
    @Test
    void findAll_ValidCategoryDto_Success() {
        CategoryDto testCategoryDto = getCategoryDto();
        Category testCategory = getCategory();

        Mockito.when(categoryRepository.findAll()).thenReturn(List.of(testCategory));
        Mockito.when(categoryMapper.toDto(testCategory)).thenReturn(testCategoryDto);

        List<CategoryDto> actual = categoryService.findAll();

        Assertions.assertEquals(1, actual.size());
        CategoryDto dto = actual.get(0);
        Assertions.assertNotNull(dto);
        EqualsBuilder.reflectionEquals(testCategoryDto, dto);
    }

    @DisplayName("Get category by ID")
    @Test
    void getById_ValidCategoryDto_Success() {
        CategoryDto testCategoryDto = getCategoryDto();
        Category testCategory = getCategory();
        Long testId = 1L;

        Mockito.when(categoryRepository.findById(testId)).thenReturn(Optional.of(testCategory));
        Mockito.when(categoryMapper.toDto(testCategory)).thenReturn(testCategoryDto);

        CategoryDto actual = categoryService.getById(testId);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(testCategoryDto, actual);
    }

    @DisplayName("Get category by non existing ID")
    @Test
    void getById_WithNonExistingId_ShouldThrowException() {
        Long testId = 100L;

        Mockito.when(categoryRepository.findById(testId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(
                RuntimeException.class,
                () -> categoryService.getById(testId)
        );
        String actual = exception.getMessage();
        String expected = "Can't find category by id: " + testId;

        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("Save new category")
    @Test
    void saveCategory_ValidCategoryDto_Success() {
        CreateCategoryRequestDto testCreateCategoryRequestDto = getCreateCategoryRequestDto();
        CategoryDto testCategoryDto = getCategoryDto();
        Category testCategory = getCategory();

        Mockito.when(categoryRepository.save(testCategory)).thenReturn(testCategory);
        Mockito.when(categoryMapper.toDto(testCategory)).thenReturn(testCategoryDto);
        Mockito.when(categoryMapper.toEntity(testCreateCategoryRequestDto))
                .thenReturn(testCategory);

        CategoryDto actual = categoryService.save(testCreateCategoryRequestDto);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(testCategoryDto, actual);
    }

    @DisplayName("Update category by ID")
    @Test
    void update_ValidCategoryDto_Success() {
        CreateCategoryRequestDto testCreateCategoryRequestDto = getCreateCategoryRequestDto();
        CategoryDto testCategoryDto = getCategoryDto();
        Category testCategory = getCategory();
        Long testId = 1L;

        Mockito.when(categoryRepository.findById(testId)).thenReturn(Optional.of(testCategory));
        Mockito.when(categoryRepository.save(testCategory)).thenReturn(testCategory);
        Mockito.when(categoryMapper.toDto(testCategory)).thenReturn(testCategoryDto);
        Mockito.when(categoryMapper.toEntity(testCreateCategoryRequestDto))
                .thenReturn(testCategory);

        CategoryDto actual = categoryService.update(testId, testCreateCategoryRequestDto);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(testCategoryDto, actual);
    }

    @DisplayName("Update category by non existing ID")
    @Test
    void update_WithNonExistingId_ShouldThrowException() {
        CreateCategoryRequestDto testCreateCategoryRequestDto = getCreateCategoryRequestDto();
        Long testId = 100L;

        Mockito.when(categoryRepository.findById(testId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(
                RuntimeException.class,
                () -> categoryService.update(testId, testCreateCategoryRequestDto)
        );
        String actual = exception.getMessage();
        String expected = "Can't find category by id: " + testId;

        Assertions.assertEquals(expected, actual);
    }

    private Category getCategory() {
        return new Category()
                .setName("Category 1")
                .setDescription("Description 1");
    }

    private CreateCategoryRequestDto getCreateCategoryRequestDto() {
        return new CreateCategoryRequestDto("Category 1", "Description 1");
    }

    private CategoryDto getCategoryDto() {
        return new CategoryDto()
                .setId(1L)
                .setName("Category 1")
                .setDescription("Description 1");
    }
}
