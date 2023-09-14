package book.store.online.service.impl;

import book.store.online.dto.request.CreateCategoryRequestDto;
import book.store.online.dto.response.CategoryDto;
import book.store.online.exception.EntityNotFoundException;
import book.store.online.mapper.CategoryMapper;
import book.store.online.model.Category;
import book.store.online.repository.book.BookSpecificationBuilder;
import book.store.online.repository.category.CategoryRepository;
import book.store.online.service.CategoryService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            return categoryMapper.toDto(optionalCategory.get());
        }
        throw new EntityNotFoundException("Can't find category by id: " + id);
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = categoryMapper.toEntity(categoryDto);
            category.setId(optionalCategory.get().getId());
            return categoryMapper.toDto(categoryRepository.save(category));
        }
        throw new EntityNotFoundException("Can't find category by id: " + id);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
