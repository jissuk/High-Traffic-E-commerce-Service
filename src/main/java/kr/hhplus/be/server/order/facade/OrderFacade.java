package kr.hhplus.be.server.order.facade;

import kr.hhplus.be.server.order.usecase.RegisterOrderItemUseCase;
import kr.hhplus.be.server.order.usecase.RegisterOrderUseCase;
import kr.hhplus.be.server.payment.usecase.RegisterPaymentUseCase;
import kr.hhplus.be.server.product.usecase.CheckProductUseCase;
import kr.hhplus.be.server.order.usecase.dto.OrderItemRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    /*
    * 1. 주문 등록
    * 2. 상품 수량 체크
    * 3. 주문 상세 등록
    * 4. 결제 및 결제 내역 등록
    * */
    private final RegisterOrderUseCase createOrderUseCase;
    private final CheckProductUseCase checkProductUseCase;
    private final RegisterOrderItemUseCase createOrderItemUseCase;
    private final RegisterPaymentUseCase createPaymentUseCase;

    public void createOrder(OrderItemRequestDTO request) {

        createOrderUseCase.execute(request.getUserId());
        checkProductUseCase.execute(request);
        createOrderItemUseCase.execute(request);
        createPaymentUseCase.execute(request);
    }

}