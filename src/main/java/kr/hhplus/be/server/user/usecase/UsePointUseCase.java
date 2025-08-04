package kr.hhplus.be.server.user.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.usecase.reader.OrderItemReader;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import kr.hhplus.be.server.user.domain.mapper.PointHistoryMapper;
import kr.hhplus.be.server.user.domain.mapper.UserMapper;
import kr.hhplus.be.server.user.domain.model.*;
import kr.hhplus.be.server.user.domain.repository.PointHistoryRepository;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.usecase.reader.UserReader;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UsePointUseCase {
    private final UserReader userReader;
    private final OrderItemReader orderItemReader;

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;

    private final UserMapper userMapper;
    private final PointHistoryMapper pointHistoryMapper;

    public void execute(PaymentCommand command) {

        User user = userReader.findUserOrThrow(command.userId());
        OrderItem orderItem = orderItemReader.findOrderItemOrThrow(command.orderId());

        user.deductPoint(orderItem.getTotalPrice());
        PointHistory pointHistory = PointHistory.use(user);

        UserEntity updateUser = userMapper.toEntity(user);
        PointHistoryEntity saveHistory = pointHistoryMapper.toEntity(pointHistory);

        userRepository.save(updateUser);
        pointHistoryRepository.save(saveHistory);
    }
}
