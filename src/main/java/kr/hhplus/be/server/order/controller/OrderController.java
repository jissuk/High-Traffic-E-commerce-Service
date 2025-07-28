package kr.hhplus.be.server.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.be.server.common.response.CommonResponse;
import kr.hhplus.be.server.order.facade.OrderFacade;
import kr.hhplus.be.server.order.usecase.command.OrderItemCommand;
import kr.hhplus.be.server.order.usecase.dto.OrderItemRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "order", description = "주문 관련 API")
public class OrderController {

    private final OrderFacade orderFacade;

    @PostMapping()
    @Operation(summary = "상품 주문", description = "유저는 아직 재고가 남아있는 상품을 주문합니다.", tags = {"OrderController"})
    public ResponseEntity<CommonResponse> createOrder(@RequestBody @Valid OrderItemRequestDTO request) {

        OrderItemCommand command = OrderItemCommand.from(request);
        orderFacade.createOrder(command);

        return ResponseEntity
                .ok()
                .body(new CommonResponse(HttpStatus.NO_CONTENT,"success",null));
    }
}