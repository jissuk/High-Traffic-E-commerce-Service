package kr.hhplus.be.server.user.usecase;


import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.coupon.exception.UserCouponNotFoundException;
import kr.hhplus.be.server.user.domain.mapper.UserResponseMapper;
import kr.hhplus.be.server.user.domain.model.PointHistory;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.PointHistoryRepository;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.usecase.command.UserCommand;
import kr.hhplus.be.server.user.usecase.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class ChargePointUseCase {
    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final UserResponseMapper userResponseMapper;

    @Transactional
    public UserResponseDTO execute(UserCommand command) {

        User user = userRepository.findById(command.userId());
        user.charegePoint(command.point());

        PointHistory pointHistory = PointHistory.charge(user);

        userRepository.save(user);
        pointHistoryRepository.save(pointHistory);

        return userResponseMapper.toDto(user);
    }
}
