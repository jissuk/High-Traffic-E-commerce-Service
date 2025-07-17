package kr.hhplus.be.server.product.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@Tag(name = "product", description = "상품 관련 API")
public class ProductController {

    @GetMapping("/{productId}")
    @Operation(summary = "상품 조회", description = "특정 상품을 조회합니다.")
    public ResponseEntity<?> getProduct() {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "상품 조회", description = "모든 상품을 조회합니다.")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/products/popular")
    @Operation(summary = "인기 판매 상품 조회", description = "현재 일을 기준으로 최근 3일간 가장 많이 판매된 상품 N개를 조회합니다.")
    public ResponseEntity<?> getPopularProducts() {
        return ResponseEntity.ok().build();
    }

}
