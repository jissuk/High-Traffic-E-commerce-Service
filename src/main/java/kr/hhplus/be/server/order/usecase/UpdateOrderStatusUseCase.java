package kr.hhplus.be.server.order.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.common.sender.OrderDataSender;
import kr.hhplus.be.server.order.domain.mapper.OrderItemMapper;
import kr.hhplus.be.server.order.domain.mapper.OrderMapper;
import kr.hhplus.be.server.order.domain.model.*;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.exception.OrderNotFoundException;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UpdateOrderStatusUseCase {

    private final OrderRepository orderRepositroy;
    private final OrderItemRepository orderItemRepository;

    private final OrderDataSender orderDataSender;

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    public void execute(PaymentCommand command) {

        OrderEntity orderEntity = findOrderOrThrow(command.orderId());
        Order order = orderMapper.toDomain(orderEntity);

        order.complete();

        OrderEntity updateOrder = orderMapper.toEntity(order);

        orderRepositroy.save(updateOrder);

        OrderItemEntity orderItemEntity = orderItemRepository.findById(command.orderItemId());
        OrderItem orderItem = orderItemMapper.toDomain(orderItemEntity);

//        orderDataSender.send(orderItem);
    }

    private OrderEntity findOrderOrThrow(long id) {
        OrderEntity order = orderRepositroy.findById(id);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        return order;
    }
}
