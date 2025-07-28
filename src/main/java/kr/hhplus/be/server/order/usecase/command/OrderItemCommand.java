package kr.hhplus.be.server.order.usecase.command;


import kr.hhplus.be.server.order.usecase.dto.OrderItemRequestDTO;

public record OrderItemCommand(Long productId, Long userId, Long orderId, Long orderItemId, Long quantity, Long price, Long totalPrice ) {
    public static OrderItemCommand from(OrderItemRequestDTO dto){
        return new OrderItemCommand(dto.getProductId(), dto.getUserId(), dto.getOrderId(), dto.getOrderItemId(), dto.getQuantity(), dto.getPrice(), dto.getTotalPrice());
    }
}

