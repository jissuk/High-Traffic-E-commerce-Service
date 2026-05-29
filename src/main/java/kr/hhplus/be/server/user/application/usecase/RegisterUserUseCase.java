package kr.hhplus.be.server.user.application.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.user.domain.mapper.UserResponseMapper;
import kr.hhplus.be.server.user.domain.model.PointHistory;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.PointHistoryRepository;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.application.usecase.command.UserCommand;
import kr.hhplus.be.server.user.presentation.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final UserResponseMapper userResponseMapper;

    @Transactional
    public UserResponse execute(UserCommand command) {
        User user = User.createBeforeUser(command);
        PointHistory pointHistory = PointHistory.charge(user);

        User result = userRepository.save(user);
        pointHistoryRepository.save(pointHistory);

        return userResponseMapper.toDto(result);
    }
}
