package kr.hhplus.be.server.user.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.order.domain.mapper.OrderItemMapper;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.exception.OrderItemNotFoundException;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequestDTO;
import kr.hhplus.be.server.user.domain.mapper.PointHistoryMapper;
import kr.hhplus.be.server.user.domain.mapper.UserMapper;
import kr.hhplus.be.server.user.domain.model.*;
import kr.hhplus.be.server.user.domain.repository.PointHistoryRepository;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UsePointUseCase {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final OrderItemRepository orderItemRepository;

    private final UserMapper userMapper;
    private final OrderItemMapper orderItemMapper;
    private final PointHistoryMapper pointHistoryMapper;

    public void execute(PaymentRequestDTO request) {
        UserEntity userEntity = findUserOrThrow(request.getUserId());
        OrderItemEntity orderItemEntity =findOrderItemOrThrow(request.getOrderItemId());

        User user = userMapper.toDomain(userEntity);
        OrderItem orderItem = orderItemMapper.toDomain(orderItemEntity);

        user.deductPoint(orderItem.getTotalPrice());
        PointHistory pointHistory = PointHistory.use(user);

        UserEntity updateUser = userMapper.toEntity(user);
        PointHistoryEntity saveHistory = pointHistoryMapper.toEntity(pointHistory);

        userRepository.update(updateUser);
        pointHistoryRepository.save(saveHistory);
    }

    private UserEntity findUserOrThrow(long id) {
        UserEntity user = userRepository.findById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    private OrderItemEntity findOrderItemOrThrow(long id) {
        OrderItemEntity orderItem = orderItemRepository.findById(id);
        if (orderItem == null) {
            throw new OrderItemNotFoundException();
        }
        return orderItem;
    }

    public void compensate(PaymentRequestDTO request) {
        UserEntity userEntity = findUserOrThrow(request.getUserId());
        OrderItemEntity orderItemEntity = findOrderItemOrThrow(request.getOrderItemId());

        User user = userMapper.toDomain(userEntity);
        OrderItem orderItem = orderItemMapper.toDomain(orderItemEntity);

        user.charegePoint(orderItem.getTotalPrice());
        PointHistory pointHistory = PointHistory.charge(user);

        UserEntity updateUser = userMapper.toEntity(user);
        PointHistoryEntity saveHistory = pointHistoryMapper.toEntity(pointHistory);

        userRepository.update(updateUser);
        pointHistoryRepository.save(saveHistory);

        System.out.println("사용자 포인트 롤백 완료: userId=" + request.getUserId() + ", 복구 포인트: " + orderItem.getTotalPrice());
        System.out.println("포인트 사용 내역 롤백 완료: userId=" + request.getUserId());
    }
}
