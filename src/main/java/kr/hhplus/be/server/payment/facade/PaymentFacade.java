package kr.hhplus.be.server.payment.facade;

import kr.hhplus.be.server.coupon.usecase.UseCouponUseCase;
import kr.hhplus.be.server.order.usecase.UpdateOrderStatusUseCase;
import kr.hhplus.be.server.payment.usecase.UpdatePaymentStatusUseCase;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import kr.hhplus.be.server.product.usecase.UpdateProductStockUseCase;
import kr.hhplus.be.server.user.usecase.UsePointUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final UsePointUseCase usePointUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final UseCouponUseCase useCouponUseCase;
    private final UpdatePaymentStatusUseCase updatePaymentStatusUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;

    /*
    * 1. 쿠폰(ID가 있을경우) 사용
    * 2. 포인트 사용
    * 3. 상품 수량 변경
    * 4. 결제 상태 변경 및 내역 등록
    * 5. 주문 상태 변경 및 내역 등록 + 외부 데이터플랫폼 요청 전송
    * */
    public void requestPayment(PaymentCommand command) {

        if(command.couponId() != null) {
            useCouponUseCase.execute(command);
        }
        usePointUseCase.execute(command);
        updateProductStockUseCase.execute(command);
        updatePaymentStatusUseCase.execute(command);
        updateOrderStatusUseCase.execute(command);

    }
}
