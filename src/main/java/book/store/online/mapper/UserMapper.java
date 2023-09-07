package book.store.online.mapper;

import book.store.online.config.MapperConfig;
import book.store.online.dto.response.UserResponseDto;
import book.store.online.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);
}
