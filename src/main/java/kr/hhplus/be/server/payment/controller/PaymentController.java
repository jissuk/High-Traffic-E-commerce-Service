package kr.hhplus.be.server.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.be.server.common.response.CommonResponse;
import kr.hhplus.be.server.payment.facade.PaymentFacade;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
@Tag(name = "payment", description = "결제 관련 API")
public class PaymentController {

    private final PaymentFacade paymentFacade;

    @PostMapping("/{userId}")
    @Operation(summary = "결제 요청", description = "유저는 취소되지 않은 주문에 대해 포인트를 사용하여 결제를 요청할 수 있습니다.")
    public ResponseEntity<CommonResponse> requestPayment(@RequestBody @Valid PaymentRequestDTO request) {

        PaymentCommand command = PaymentCommand.from(request);

        paymentFacade.requestPayment(command);

        return ResponseEntity.ok().build();
    }
}