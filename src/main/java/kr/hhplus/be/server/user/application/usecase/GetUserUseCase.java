package kr.hhplus.be.server.user.application.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.user.domain.mapper.UserResponseMapper;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.presentation.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class GetUserUseCase {

    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;

    @Transactional(readOnly = true)
    public UserResponse execute(long userId) {
        User user = userRepository.findById(userId);
        return userResponseMapper.toDto(user);
    }
}
