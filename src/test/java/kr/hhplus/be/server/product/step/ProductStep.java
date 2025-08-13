package kr.hhplus.be.server.product.step;

import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.model.ProductEntity;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class ProductStep {

    private static String PATH_URL = "/products";

    public static Product 상품_기본값(){

        return Product.builder()
                .productName("기본 상품")
                .price(2000L)
                .quantity(5L)
                .build();
    }

    public static ProductEntity 상품엔티티_기본값(){

        return ProductEntity.builder()
                            .productName("기본 상품")
                            .price(2000L)
                            .quantity(200L)
                            .build();
    }

    public static List<Product> 전체상품_기본값(){
        List<Product> productList = new ArrayList<>();
        Product product = Product.builder()
                                .productName("기본 상품")
                                .price(2000L)
                                .quantity(5L)
                                .build();
        productList.add(product);
        return productList;
    }

    public static ResultActions 단건상품조회요청(MockMvc mockMvc, long prodcutId) throws Exception {
        return mockMvc.perform(get(PATH_URL + "/{productId}", prodcutId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print());
    }

    public static ResultActions 전체상품조회요청(MockMvc mockMvc) throws Exception {
        return mockMvc.perform(get(PATH_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print());
    }

    public static ResultActions 인기판매상품조회요청(MockMvc mockMvc) throws Exception {
        return mockMvc.perform(get(PATH_URL + "/popular")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
