package kr.hhplus.be.server.product.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.response.CommonResponse;
import kr.hhplus.be.server.product.usecase.GetAllProductUseCase;
import kr.hhplus.be.server.product.usecase.GetPopularProductUseCase;
import kr.hhplus.be.server.product.usecase.GetProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@Tag(name = "product", description = "상품 관련 API")
public class ProductController {

    private final GetProductUseCase getProductUseCase;
    private final GetAllProductUseCase getAllProductUseCase;
    private final GetPopularProductUseCase getPopularProductUseCase;

    @GetMapping("/{productId}")
    @Operation(summary = "상품 조회", description = "특정 상품을 조회합니다.")
    public ResponseEntity<CommonResponse> getProduct(@PathVariable int productId) {

        return ResponseEntity
                .ok()
                .body(new CommonResponse(HttpStatus.OK, "success", getProductUseCase.execute(productId)));
    }

    @GetMapping
    @Operation(summary = "모든 상품 조회", description = "모든 상품을 조회합니다.")

    public ResponseEntity<CommonResponse> getAllProducts() {
        return ResponseEntity
                .ok()
                .body(new CommonResponse(HttpStatus.OK, "success", getAllProductUseCase.execute()));
    }

    @GetMapping("/popular")
    @Operation(summary = "인기 판매 상품 조회", description = "현재 일을 기준으로 최근 3일간 가장 많이 판매된 상품 N개를 조회합니다.")
    public ResponseEntity<CommonResponse> getPopularProducts() throws JsonProcessingException {
        return ResponseEntity
                .ok()
                .body(new CommonResponse(HttpStatus.OK, "success", getPopularProductUseCase.execute()));
    }

}
