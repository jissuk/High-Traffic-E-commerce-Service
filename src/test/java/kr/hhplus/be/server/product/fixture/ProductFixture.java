package kr.hhplus.be.server.product.fixture;

import kr.hhplus.be.server.product.domain.model.Product;

public class ProductFixture {

    private Long id;
    private String productName;
    private Long price;
    private Long quantity;

    private ProductFixture(){
        this.id = 0L;
        this.productName = "기본 상품";
        this.price = 2000L;
        this.quantity = 5L;
    }

    public static ProductFixture builder(){
        return new ProductFixture();
    }

    public ProductFixture id(Long id){
        this.id = id;
        return this;
    }

    public Product build(){
        return Product.builder()
                .id(id)
                .productName(productName)
                .price(price)
                .quantity(quantity)
                .build();
    }

//    public static Product create(){
//        return Product.builder()
//                .productName("기본 상품")
//                .price(2000L)
//                .quantity(5L)
//                .build();
//    }

//    public static Product withId(long id){
//        return Product.builder()
//                .id(id)
//                .productName("기본 상품")
//                .price(2000L)
//                .quantity(5L)
//                .build();
//    }
}
