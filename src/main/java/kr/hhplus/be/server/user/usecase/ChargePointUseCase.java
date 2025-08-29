package kr.hhplus.be.server.user.usecase;


import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.user.domain.mapper.UserResponseMapper;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.event.PointChargeEvent;
import kr.hhplus.be.server.user.usecase.command.UserCommand;
import kr.hhplus.be.server.user.usecase.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@UseCase
@RequiredArgsConstructor
public class ChargePointUseCase {

    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;

    private final TransactionTemplate transactionTemplate;

    private final ApplicationEventPublisher applicationEventPublisher;

    public UserResponse execute(UserCommand command) {

        User user = userRepository.findById(command.userId());

        user.charegePoint(command.point());

        transactionTemplate.executeWithoutResult(status -> {
            userRepository.save(user);
            applicationEventPublisher.publishEvent(PointChargeEvent.from(command.userId(), command.point()));
        });

        return userResponseMapper.toDto(user);
    }
}
