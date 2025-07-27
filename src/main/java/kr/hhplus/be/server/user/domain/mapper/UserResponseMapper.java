package kr.hhplus.be.server.user.domain.mapper;

import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.usecase.dto.UserResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UserResponseMapper {
    UserResponseDTO toDto(User user);
}
