package kr.hhplus.be.server.order.facade;

import kr.hhplus.be.server.order.usecase.RegisterOrderItemUseCase;
import kr.hhplus.be.server.order.usecase.RegisterOrderUseCase;
import kr.hhplus.be.server.order.usecase.command.OrderItemCommand;
import kr.hhplus.be.server.payment.usecase.RegisterPaymentUseCase;
import kr.hhplus.be.server.product.usecase.CheckProductUseCase;
import kr.hhplus.be.server.order.usecase.dto.OrderItemRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private final RegisterOrderItemUseCase registerOrderItemUseCase;
    private final RegisterPaymentUseCase registerPaymentUseCase;

    @Transactional
    public void createOrder(OrderItemCommand command) {

        createOrderUseCase.execute(command.userId());
        checkProductUseCase.execute(command);
        registerOrderItemUseCase.execute(command);
        registerPaymentUseCase.execute(command);
    }

}