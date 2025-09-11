package kr.hhplus.be.server.user.domain.mapper;

import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toDomain(UserEntity userEntity){
        return User.builder()
                    .id(userEntity.getId())
                    .point(userEntity.getPoint())
                    .version(userEntity.getVersion())
                    .build();
    };

    public UserEntity toEntity(User user){
        return UserEntity.builder()
                            .id(user.getId())
                            .point(user.getPoint())
                            .version(user.getVersion())
                            .build();
    }
}
