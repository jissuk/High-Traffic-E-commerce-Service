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
import org.springframework.transaction.support.TransactionTemplate;

@UseCase
@RequiredArgsConstructor
public class ChargePointUseCase {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final UserResponseMapper userResponseMapper;
    private final TransactionTemplate transactionTemplate;

    public UserResponse execute(UserCommand command) {
        User user = userRepository.findById(command.userId());
        user.chargePoint(command.point());

        transactionTemplate.executeWithoutResult(status -> {
            userRepository.save(user);
            PointHistory pointHistory = PointHistory.charge(user);
            pointHistoryRepository.save(pointHistory);
        });

        return userResponseMapper.toDto(user);
    }
}
