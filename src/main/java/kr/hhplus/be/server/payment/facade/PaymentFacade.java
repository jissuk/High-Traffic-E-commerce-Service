package kr.hhplus.be.server.payment.facade;

import kr.hhplus.be.server.coupon.usecase.UseCouponUseCase;
import kr.hhplus.be.server.order.usecase.UpdateOrderStatusUseCase;
import kr.hhplus.be.server.payment.usecase.UpdatePaymentStatusUseCase;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequestDTO;
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
    public void requestPayment(PaymentRequestDTO request) {
        boolean couponUsed = false;
        boolean pointUsed = false;
        boolean stockUpdated = false;
        boolean paymentStatusUpdated = false;

        try {
            if(request.getCouponId() != null) {
                useCouponUseCase.execute(request);
                couponUsed = true;
            }

            usePointUseCase.execute(request);
            pointUsed = true;

            updateProductStockUseCase.execute(request);
            stockUpdated = true;

            updatePaymentStatusUseCase.execute(request);
            paymentStatusUpdated = true;

            updateOrderStatusUseCase.execute(request);

        } catch (Exception e) {
            if (paymentStatusUpdated) {
                updatePaymentStatusUseCase.compensate(request);
            }
            if (stockUpdated) {
                updateProductStockUseCase.compensate(request);
            }
            if (pointUsed) {
                usePointUseCase.compensate(request);
            }
            if (couponUsed) {
                useCouponUseCase.compensate(request);
            }
            throw new RuntimeException("결제 처리 중 오류가 발생하여 작업이 롤백되었습니다.", e);
        }
    }
}
