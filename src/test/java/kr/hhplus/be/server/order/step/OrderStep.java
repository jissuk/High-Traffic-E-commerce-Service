package kr.hhplus.be.server.order.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.order.domain.model.OrderEntity;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.domain.model.OrderStatus;
import kr.hhplus.be.server.order.usecase.command.OrderItemCommand;
import kr.hhplus.be.server.order.usecase.dto.OrderItemRequestDTO;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class OrderStep {

    private static String PATH_URL = "/orders";

    public static OrderItemCommand 주문상세커맨드_기본값(){
        return new OrderItemCommand(1L, 1L, 1L, 1L, 2L, 3000L, 6000L);
    }

    public static OrderEntity 주문엔티티_기본값(){
        return OrderEntity.builder()
                    .orderStatus(OrderStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();
    }

    public static OrderItemEntity 주문상세엔티티_기본값(){
        return OrderItemEntity.builder()
                        .quantity(2L)
                        .price(3000L)
                        .totalPrice(6000L)
                        .build();
    }

    public static OrderItemRequestDTO 주문상세요청_기본값(){
        return OrderItemRequestDTO.builder()
                                    .userId(1L)
                                    .orderItemId(1L)
                                    .productId(1L)
                                    .orderId(1L)
                                    .quantity(1L)
                                    .price(3000L)
                                    .build();
    }


    public static ResultActions 주문요청(MockMvc mockMvc, ObjectMapper objectMapper, OrderItemRequestDTO request, long userId) throws Exception {
        return mockMvc.perform(post(PATH_URL+"/{userId}", userId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print());
    }


}
