package kr.hhplus.be.server.user.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.user.domain.mapper.UserResponseMapper;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.usecase.dto.UserResponse;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetUserUseCase {

    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;

    public UserResponse execute(long userId) {
        User user = userRepository.findById(userId);
        return userResponseMapper.toDto(user);
    }
}
