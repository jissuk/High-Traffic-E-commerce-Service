package kr.hhplus.be.server.order.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.order.domain.mapper.OrderHistoryMapper;
import kr.hhplus.be.server.order.domain.mapper.OrderMapper;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderHistory;
import kr.hhplus.be.server.user.domain.mapper.UserMapper;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.exception.UserNotFoundException;
import kr.hhplus.be.server.order.domain.model.OrderEntity;
import kr.hhplus.be.server.order.domain.model.OrderHistoryEntity;
import kr.hhplus.be.server.order.domain.repository.OrderHistoryRepository;
import kr.hhplus.be.server.order.domain.repository.OrderRepositroy;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RegisterOrderUseCase {

    private final UserRepository userRepository;

    private final OrderRepositroy orderRepositroy;
    private final OrderHistoryRepository orderHistoryRepository;

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final OrderHistoryMapper orderItemMapper;

    public void execute(long userId) {

        UserEntity userEntity = findUserOrThrow(userId);
        User user = userMapper.toDomain(userEntity);

        Order order = Order.createBeforeOrder(user);
        OrderHistory orderHistory = OrderHistory.of(order);

        OrderEntity orderEntity = orderMapper.toEntity(order);
        OrderHistoryEntity historyEntity = orderItemMapper.toEntity(orderHistory);

        orderRepositroy.save(orderEntity);
        orderHistoryRepository.save(historyEntity);
    }

    private UserEntity findUserOrThrow(long id) {
        UserEntity user = userRepository.findById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }
}
