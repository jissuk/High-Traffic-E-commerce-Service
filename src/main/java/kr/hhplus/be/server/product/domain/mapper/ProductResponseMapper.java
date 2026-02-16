package kr.hhplus.be.server.product.domain.mapper;

import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.usecase.dto.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductResponseMapper {
    public ProductResponse toDto(Product domain){
        return ProductResponse.builder()
                .id(domain.getId())
                .productName(domain.getProductName())
                .price(domain.getPrice())
                .quantity(domain.getQuantity())
                .build();
    }
}