package kr.hhplus.be.server.user.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.user.domain.model.PointHistory;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.PointHistoryRepository;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.usecase.command.UserCommand;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public void execute(UserCommand command) {
        User user = User.createBeforeUser(command);
        PointHistory pointHistory = PointHistory.charge(user);

        userRepository.save(user);
        pointHistoryRepository.save(pointHistory);
    }
}
