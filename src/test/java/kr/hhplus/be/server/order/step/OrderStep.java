package kr.hhplus.be.server.order.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.model.OrderStatus;
import kr.hhplus.be.server.order.usecase.command.OrderCommand;
import kr.hhplus.be.server.order.usecase.dto.OrderRequest;
import kr.hhplus.be.server.user.domain.model.User;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class OrderStep {
    private static String PATH_URL = "/orders";

    public static OrderCommand defaultOrderCommand(){
        return OrderCommand.builder()
                            .productId(1L)
                            .userId(1L)
                            .quantity(2L)
                            .build();
    }

    public static Order defaultOrder(){
        return Order.builder()
                    .id(1L)
                    .orderStatus(OrderStatus.CREATED)
                    .createdAt(LocalDateTime.now())
                    .totalPrice(50_000L)
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


    public static OrderRequest defaultOrderRequest(){
        return OrderRequest.builder()
                                    .userId(1L)
                                    .productId(1L)
                                    .quantity(1L)
                                    .build();
    }

    public static OrderRequest orderRequestWithUserId(long userId){
        return OrderRequest.builder()
                            .userId(userId)
                            .productId(1L)
                            .quantity(1L)
                            .build();
    }

    public static OrderRequest orderRequestWithProductId(long productId){
        return OrderRequest.builder()
                .userId(1L)
                .productId(productId)
                .quantity(1L)
                .build();
    }

    public static OrderItem orderItemWithProductIdAndQuantity(long productId, long quantity) {
        return OrderItem.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }

    public static ResultActions orderRequest(MockMvc mockMvc, ObjectMapper objectMapper, OrderRequest request) throws Exception {
        return mockMvc.perform(post(PATH_URL)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print());
    }
}
