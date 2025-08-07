//package kr.hhplus.be.server.order.facade;
//
//import kr.hhplus.be.server.order.usecase.RegisterOrderUseCase;
//import kr.hhplus.be.server.order.usecase.command.OrderItemCommand;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//@Component
//@RequiredArgsConstructor
//public class OrderFacade {
//
//    /*
//    * 1. 주문 등록
//    * 2. 상품 수량 체크
//    * 3. 주문 상세 등록
//    * 4. 결제 및 결제 내역 등록
//    * */
//    private final RegisterOrderUseCase2 registerOrderUseCase2;
//    private final CheckProductUseCase checkProductUseCase;
//    private final RegisterOrderItemUseCase registerOrderItemUseCase;
//    private final RegisterPaymentUseCase2 registerPaymentUseCase;
//
//    private final RegisterOrderUseCase registerOrderUseCase;
//    @Transactional
//    public void createOrder(OrderItemCommand command) {
//
//        registerOrderUseCase2.execute(command.userId());
//        checkProductUseCase.execute(command);
//        registerOrderItemUseCase.execute(command);
//        registerPaymentUseCase.execute(command);
//    }
//
//}