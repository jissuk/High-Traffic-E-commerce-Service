package kr.hhplus.be.server.order.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.model.OrderStatus;
import kr.hhplus.be.server.order.usecase.command.OrderCommand;
import kr.hhplus.be.server.user.domain.model.User;

import java.time.LocalDateTime;


public class OrderStep {

    public static OrderCommand defaultOrderCommand(){
        return OrderCommand.builder()
                            .productId(1L)
                            .userId(1L)
                            .quantity(2L)
                            .build();
    }

    public static Order orderWithUserId(User user){
        return Order.builder()
                .id(1L)
                .orderStatus(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .totalPrice(50_000L)
                .userId(user.getId())
                .build();
    }
    public static OrderItem defaultOrderItem(Order order){
        return OrderItem.builder()
                        .id(1L)
                        .orderId(order.getId())
                        .quantity(1L)
                        .productId(1L)
                        .build();
    }

    public static OrderItem orderItemWithProductIdAndQuantity(long productId, long quantity) {
        return OrderItem.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}
