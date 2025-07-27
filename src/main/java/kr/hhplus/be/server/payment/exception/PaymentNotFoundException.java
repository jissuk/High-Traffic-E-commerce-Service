package kr.hhplus.be.server.payment.exception;

public class PaymentNotFoundException extends PaymentOperationException {

    private static final String DEFAULT_MESSAGE = "상품 조회에 실패하였습니다.";

    public PaymentNotFoundException(){
        super(DEFAULT_MESSAGE);
    }
    public PaymentNotFoundException(String message) {
        super(message);
    }
}

