package book.store.online.mapper;

import book.store.online.config.MapperConfig;
import book.store.online.dto.request.UserLoginRequestDto;
import book.store.online.dto.response.UserLoginResponseDto;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface LoginMapper {
    UserLoginResponseDto toDto(String token);

    String toModel(UserLoginRequestDto requestDto);
}
