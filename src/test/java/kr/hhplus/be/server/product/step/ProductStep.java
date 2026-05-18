package kr.hhplus.be.server.product.step;

import kr.hhplus.be.server.product.domain.model.Product;

public class ProductStep {

    public static Product productWithProductId(long id){
        return Product.builder()
                .id(id)
                .productName("기본 상품")
                .price(2000L)
                .quantity(5L)
                .build();
    }
}
