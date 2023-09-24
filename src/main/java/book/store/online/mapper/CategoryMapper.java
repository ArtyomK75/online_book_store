package book.store.online.mapper;

import book.store.online.config.MapperConfig;
import book.store.online.dto.request.CreateCategoryRequestDto;
import book.store.online.dto.response.CategoryDto;
import book.store.online.model.Category;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequestDto categoryDto);

    @Named("categoryFromId")
    default Set<Category> categoryFromId(List<Long> categoryIds) {
        if (categoryIds == null) {
            return null;
        }
        Set<Category> categories = new HashSet<>();
        for (Long id: categoryIds) {
            Category category = new Category();
            category.setId(id);
            categories.add(category);
        }
        return categories;
    }
}
