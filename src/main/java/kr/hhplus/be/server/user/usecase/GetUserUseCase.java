package kr.hhplus.be.server.user.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.user.domain.mapper.UserResponseMapper;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.usecase.dto.UserResponseDTO;
import kr.hhplus.be.server.user.usecase.reader.UserReader;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetUserUseCase {

    private final UserReader userReader;
    private final UserResponseMapper userResponseMapper;

    public UserResponseDTO execute(long userId) {

        User user = userReader.findUserOrThrow(userId);
        return userResponseMapper.toDto(user);
    }

}
