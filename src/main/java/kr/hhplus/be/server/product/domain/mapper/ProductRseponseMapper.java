package kr.hhplus.be.server.product.domain.mapper;

import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.usecase.dto.ProductResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductRseponseMapper {
    public ProductResponseDTO toDto(Product domain){
        return ProductResponseDTO.builder()
                .id(domain.getId())
                .productName(domain.getProductName())
                .price(domain.getPrice())
                .quantity(domain.getQuantity())
                .build();
    }
}