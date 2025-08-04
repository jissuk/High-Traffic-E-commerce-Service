package kr.hhplus.be.server.order.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.order.domain.mapper.OrderMapper;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.user.domain.mapper.UserMapper;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.exception.UserNotFoundException;
import kr.hhplus.be.server.order.domain.model.OrderEntity;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.usecase.reader.UserReader;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RegisterOrderUseCase {

    private final UserReader userReader;
    
    private final UserRepository userRepository;

    private final OrderRepository orderRepositroy;

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;

    public void execute(long userId) {

        User user = userReader.findUserOrThrow(userId);

        Order order = Order.createBeforeOrder(user);
        OrderEntity orderEntity = orderMapper.toEntity(order);

        orderRepositroy.save(orderEntity);
    }
}
