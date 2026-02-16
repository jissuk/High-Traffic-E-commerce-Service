//package kr.hhplus.be.server.payment.step;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
//import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
//import kr.hhplus.be.server.payment.domain.model.PaymentStatus;
//import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
//import kr.hhplus.be.server.user.domain.model.UserEntity;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//
//public class PaymentStep {
//
//    private static String PATH_URL = "/payments";
//
//    public static PaymentCommand defaultPaymentCommand() {
//        return new PaymentCommand(1L, 1L, 1L, 1L, 1L);
//    }
//
//    public static PaymentEntity paymentEntityWithOrderItemIdAndCreateAt(long id, LocalDateTime createdAt) {
//        return PaymentEntity.builder()
//                .price(3000L)
//                .paymentStatus(PaymentStatus.COMPLETED)
//                .orderItemId(id)
//                .createAt(createdAt)
//                .build();
//    }
//
//    public static PaymentEntity defaultPaymentEntity(UserEntity user, OrderItemEntity orderItem) {
//        return PaymentEntity.builder()
//                .price(3000L)
//                .paymentStatus(PaymentStatus.PENDING)
//                .createAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
//                .userId(user.getId())
//                .orderItemId(orderItem.getId())
//                .build();
//    }
//
//    public static PaymentRequestRequest defaultPaymentRequest(){
//        return PaymentRequestRequest.builder()
//                                .userId(1L)
//                                .orderId(1L)
//                                .orderItemId(1L)
//                                .couponId(1L)
//                                .productId(1L)
//                                .build();
//    }
//
//    public static PaymentRequestRequest paymentRequestWithCouponId(Long couponId){
//        return PaymentRequestRequest.builder()
//                                .userId(1L)
//                                .orderId(1L)
//                                .orderItemId(1L)
//                                .couponId(couponId)
//                                .productId(1L)
//                                .build();
//    }
//
//    public static PaymentRequestRequest paymentRequestWithUserId(long userId){
//        return PaymentRequestRequest.builder()
//                .userId(userId)
//                .orderId(1L)
//                .orderItemId(1L)
//                .couponId(1L)
//                .productId(1L)
//                .build();
//    }
//
//    public static PaymentRequestRequest paymentRequestWithOrderId(long orderId){
//        return PaymentRequestRequest.builder()
//                .userId(1L)
//                .orderId(orderId)
//                .orderItemId(1L)
//                .couponId(1L)
//                .productId(1L)
//                .build();
//    }
//
//    public static PaymentRequestRequest paymentRequestWithOrderItemId(long orderItemId){
//        return PaymentRequestRequest.builder()
//                .userId(1L)
//                .orderId(1L)
//                .orderItemId(orderItemId)
//                .couponId(1L)
//                .productId(1L)
//                .build();
//    }
//
//    public static PaymentRequestRequest paymentRequestWithPaymentId(long paymentId){
//        return PaymentRequestRequest.builder()
//                .userId(1L)
//                .orderId(1L)
//                .orderItemId(1L)
//                .couponId(1L)
//                .productId(1L)
//                .build();
//    }
//
//    public static PaymentRequestRequest paymentRequestWithProductId(long productId){
//        return PaymentRequestRequest.builder()
//                .userId(1L)
//                .orderId(1L)
//                .orderItemId(1L)
//                .couponId(1L)
//                .productId(productId)
//                .build();
//    }
//
//    public static ResultActions paymentRequest(MockMvc mockMvc, ObjectMapper objectMapper, PaymentRequestRequest request) throws Exception {
//
//        return mockMvc.perform(post(PATH_URL)
//                                    .content(objectMapper.writeValueAsString(request))
//                                    .contentType(MediaType.APPLICATION_JSON))
//                                    .andDo(print());
//
//    }
//
//}
