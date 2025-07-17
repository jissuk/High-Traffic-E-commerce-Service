package kr.hhplus.be.server.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@Tag(name = "payment", description = "결제 관련 API")
public class PaymentController {

    @PostMapping
    @Operation(summary = "결제 요청", description = "유저는 취소되지 않은 주문에 대해 포인트를 사용하여 결제를 요청할 수 있습니다.")
    public ResponseEntity<?> requestPayment() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "결제 조회", description = "유저는 주문의 결제 상태를 조회할 수 있습니다.")
    public ResponseEntity<?> getPaymentById(@PathVariable int paymentId){
        return ResponseEntity.ok().build();
    }
}