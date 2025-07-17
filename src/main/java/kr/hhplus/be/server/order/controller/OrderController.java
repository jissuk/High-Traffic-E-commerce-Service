package kr.hhplus.be.server.order.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@Tag(name = "order", description = "주문 관련 API")
public class OrderController {

    @PostMapping
    @Operation(summary = "상품 주문", description = "유저는 아직 재고가 남아있는 상품을 주문합니다.", tags = {"OrderController"})
    public ResponseEntity<?> createOrder(){
        return ResponseEntity.ok().build();
    }

    @GetMapping("{orderId}")
    @Operation(summary = "주문 조회", description = "유저는 단건 주문의 내용을 조회합니다.", tags = {"OrderController"})
    public ResponseEntity<?> getOrder(){
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "전체 주문 조회", description = "유저는 모든 주문의 내용을 조회합니다.", tags = {"OrderController"})
    public ResponseEntity<?> getAllOrders(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("{orderId}/cancel")
    @Operation(summary = "주문 취소", description = "유저는 [결제전, 결제 완료, 배송전] 상태의 주문을 취소할 수 있습니다.", tags = {"OrderController"})
    public ResponseEntity<?> cancelOrder(){
        return ResponseEntity.ok().build();
    }

}
