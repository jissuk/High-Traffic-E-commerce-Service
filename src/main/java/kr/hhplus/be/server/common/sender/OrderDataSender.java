package kr.hhplus.be.server.common.sender;

import kr.hhplus.be.server.order.domain.model.OrderItem;

public interface OrderDataSender {
    void send(OrderItem order);
}
