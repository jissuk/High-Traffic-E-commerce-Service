package kr.hhplus.be.server.user.usecase.command;


import kr.hhplus.be.server.user.usecase.dto.UserRequestDTO;

public record UserCommand (Long userId, Long point){

    public static UserCommand from (UserRequestDTO dto) {
        return new UserCommand(dto.getUserId(), dto.getPoint());
    }
}
