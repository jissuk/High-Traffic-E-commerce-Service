package kr.hhplus.be.server.user.domain.mapper;

import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.usecase.dto.UserResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper {
    public UserResponseDTO toDto(User user){
        return UserResponseDTO.builder()
                                .userId(user.getUserId())
                                .point(user.getPoint())
                                .build();
    };
}
