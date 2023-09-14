package book.store.online.service;

import book.store.online.dto.request.CreateCategoryRequestDto;
import book.store.online.dto.response.CategoryDto;
import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll();

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryRequestDto categoryDto);

    CategoryDto update(Long id, CreateCategoryRequestDto categoryDto);

    void deleteById(Long id);
}
