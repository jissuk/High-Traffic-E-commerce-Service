package kr.hhplus.be.server.user.domain.mapper;

import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toDomain(UserEntity userEntity);
    UserEntity toEntity(User user);
}
