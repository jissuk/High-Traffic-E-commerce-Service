//package kr.hhplus.be.server.order.step;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import kr.hhplus.be.server.order.domain.model.*;
//import kr.hhplus.be.server.order.usecase.command.OrderCommand;
//import kr.hhplus.be.server.order.usecase.dto.OrderRequest;
//import kr.hhplus.be.server.user.domain.model.UserEntity;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import java.time.LocalDateTime;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//
//public class OrderStep {
//
//    private static String PATH_URL = "/orders";
//
//    public static OrderCommand defaultOrderCommand(){
//        return OrderCommand.builder()
//                .productId(1L)
//                .userId(1L)
//                .quantity(2L)
//                .price(3000L)
//                .build();
//    }
//
//    public static OrderEntity defaultOrderEntity(UserEntity user){
//        return OrderEntity.builder()
//                .orderStatus(OrderStatus.PENDING)
//                .createdAt(LocalDateTime.now())
//                .userId(user.getId())
//                .build();
//    }
//
//    public static OrderItemEntity defaultOrderItemEntity(OrderEntity order){
//        return OrderItemEntity.builder()
//                .quantity(2L)
//                .price(3000L)
//                .totalPrice(6000L)
//                .orderId(order.getId())
//                .build();
//    }
//
//    public static OrderItemEntity orderItemWithProductIdAndQuantity(long productId, long quantity) {
//        return OrderItemEntity.builder()
//                .productId(productId)
//                .quantity(quantity)
//                .price(3000L)
//                .totalPrice(6000L)
//                .build();
//    }
//
//
//    public static OrderRequest defaultOrderItemRequest(){
//        return OrderRequest.builder()
//                                    .userId(1L)
//                                    .productId(1L)
//                                    .quantity(1L)
//                                    .price(3000L)
//                                    .build();
//    }
//
//    public static OrderRequest orderItemRequestWithUserId(long userId){
//        return OrderRequest.builder()
//                .userId(userId)
//                .productId(1L)
//                .quantity(1L)
//                .price(3000L)
//                .build();
//    }
//
//    public static OrderRequest orderItemRequestWithProductId(long productId){
//        return OrderRequest.builder()
//                .userId(1L)
//                .productId(productId)
//                .quantity(1L)
//                .price(3000L)
//                .build();
//    }
//
//    public static ResultActions orderRequest(MockMvc mockMvc, ObjectMapper objectMapper, OrderRequest request) throws Exception {
//        return mockMvc.perform(post(PATH_URL)
//                        .content(objectMapper.writeValueAsString(request))
//                        .contentType(MediaType.APPLICATION_JSON))
//                        .andDo(print());
//    }
//}
