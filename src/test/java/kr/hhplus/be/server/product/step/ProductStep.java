package kr.hhplus.be.server.product.step;

import kr.hhplus.be.server.product.domain.model.ProductEntity;

import java.util.HashMap;
import java.util.Map;

public class ProductStep {

    public static ProductEntity 상품엔티티_기본값(){

        return ProductEntity.builder()
                        .productName("기본 상품")
                        .price(2000L)
                        .quantity(5L)
                        .build();
    }

    public static Map<Long, ProductEntity> 전체상품엔티티_기본값(){
        Map<Long, ProductEntity> productMap = new HashMap<>();
        ProductEntity product = ProductEntity.builder()
                                    .productName("기본 상품")
                                    .price(2000L)
                                    .quantity(5L)
                                    .build();
        productMap.put(1L, product);
        return productMap;
    }
}
