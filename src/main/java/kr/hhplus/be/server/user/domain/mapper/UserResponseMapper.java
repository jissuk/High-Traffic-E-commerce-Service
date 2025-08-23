package kr.hhplus.be.server.user.domain.mapper;

import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.usecase.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper {
    public UserResponse toDto(User user){
        return UserResponse.builder()
                                .userId(user.getId())
                                .point(user.getPoint())
                                .build();
    };
}
