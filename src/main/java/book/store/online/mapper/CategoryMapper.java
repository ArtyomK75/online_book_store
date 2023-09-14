package book.store.online.mapper;

import book.store.online.config.MapperConfig;
import book.store.online.dto.request.CreateCategoryRequestDto;
import book.store.online.dto.response.CategoryDto;
import book.store.online.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequestDto categoryDto);
}
