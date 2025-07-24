package kr.hhplus.be.server.product.step;

import kr.hhplus.be.server.product.domain.model.ProductEntity;

import java.util.HashMap;
import java.util.Map;

public class ProductStep {

    public static ProductEntity 기본상품엔티티생성(){

        return ProductEntity.builder()
                        .productName("기본 상품")
                        .price(2000L)
                        .quantity(5L)
                        .build();
    }

    public static Map<Long, ProductEntity> 기본전체상품엔티티생성(){
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
