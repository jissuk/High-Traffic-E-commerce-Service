package kr.hhplus.be.server.user.domain.mapper;

import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toDomain(UserEntity userEntity){
        return User.builder()
                    .userId(userEntity.getId())
                    .point(userEntity.getPoint())
                    .build();
    };

    public UserEntity toEntity(User user){
        return UserEntity.builder()
                            .id(user.getUserId())
                            .point(user.getPoint())
                            .build();
    }
}
