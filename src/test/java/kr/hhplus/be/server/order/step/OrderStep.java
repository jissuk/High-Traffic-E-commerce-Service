package kr.hhplus.be.server.order.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.order.domain.model.*;
import kr.hhplus.be.server.order.usecase.dto.OrderItemRequest;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class OrderStep {

    private static String PATH_URL = "/orders";



    public static OrderEntity 주문엔티티_기본값(UserEntity user){
        return OrderEntity.builder()
                .orderStatus(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .userId(user.getId())
                .build();
    }

    public static OrderItem 주문상세_기본값() {
        return OrderItem.builder()
                .quantity(2L)
                .price(3000L)
                .totalPrice(6000L)
                .build();
    }

    public static OrderItemEntity 주문상세엔티티_기본값(){
        return OrderItemEntity.builder()
                .quantity(2L)
                .price(3000L)
                .totalPrice(6000L)
                .build();
    }

    public static OrderItemEntity 주문상세엔티티_기본값(OrderEntity order){
        return OrderItemEntity.builder()
                .quantity(2L)
                .price(3000L)
                .totalPrice(6000L)
                .orderId(order.getId())
                .build();
    }
    public static OrderItemEntity 주문상세엔티티_기본값_상품ID지정(long id) {
        return OrderItemEntity.builder()
                .quantity(2L)
                .price(3000L)
                .totalPrice(6000L)
                .productId(id)
                .build();
    }
    public static OrderItemEntity 주문상세엔티티_기본값_상품ID_수량_지정(long productId, long quantity) {
        return OrderItemEntity.builder()
                .productId(productId)
                .quantity(quantity)
                .price(3000L)
                .totalPrice(6000L)
                .build();
    }


    public static OrderItemRequest 주문상세요청_기본값(){
        return OrderItemRequest.builder()
                                    .userId(1L)
                                    .productId(1L)
                                    .orderId(1L)
                                    .quantity(1L)
                                    .price(3000L)
                                    .build();
    }

    public static OrderItemRequest 주문상세요청_유저ID지정(long userId){
        return OrderItemRequest.builder()
                .userId(userId)
                .productId(1L)
                .orderId(1L)
                .quantity(1L)
                .price(3000L)
                .build();
    }

    public static OrderItemRequest 주문상세요청_상품ID지정(long productId){
        return OrderItemRequest.builder()
                .userId(1L)
                .productId(productId)
                .orderId(1L)
                .quantity(1L)
                .price(3000L)
                .build();
    }

    public static ResultActions 주문요청(MockMvc mockMvc, ObjectMapper objectMapper, OrderItemRequest request) throws Exception {
        return mockMvc.perform(post(PATH_URL)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print());
    }
}
