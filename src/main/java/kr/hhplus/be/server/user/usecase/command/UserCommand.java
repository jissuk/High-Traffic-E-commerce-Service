package kr.hhplus.be.server.user.usecase.command;

import kr.hhplus.be.server.user.usecase.dto.UserRequest;
import lombok.Builder;

@Builder
public record UserCommand (Long userId, Long point){

    public static UserCommand from (UserRequest dto) {
        return UserCommand.builder()
                            .userId(dto.userId())
                            .point(dto.point())
                            .build();
    }
}
