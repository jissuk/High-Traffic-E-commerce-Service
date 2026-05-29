package kr.hhplus.be.server.product.fixture;

import kr.hhplus.be.server.product.domain.model.Product;

public class ProductFixture {

    public static Product create(){
        return Product.builder()
                .productName("기본 상품")
                .price(2000L)
                .quantity(5L)
                .build();
    }

    public static Product withProductId(long id){
        return Product.builder()
                .id(id)
                .productName("기본 상품")
                .price(2000L)
                .quantity(5L)
                .build();
    }
}
