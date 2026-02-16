package kr.hhplus.be.server.user.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.user.domain.mapper.UserResponseMapper;
import kr.hhplus.be.server.user.domain.model.PointHistory;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.PointHistoryRepository;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.usecase.command.UserCommand;
import kr.hhplus.be.server.user.usecase.dto.UserResponse;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final UserResponseMapper userResponseMapper;

    public UserResponse execute(UserCommand command) {
        User user = User.createBeforeUser(command);
        PointHistory pointHistory = PointHistory.charge(user);

        User result = userRepository.save(user);
        pointHistoryRepository.save(pointHistory);

        return userResponseMapper.toDto(result);
    }
}
