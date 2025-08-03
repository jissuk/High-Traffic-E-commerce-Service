package kr.hhplus.be.server.product.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.product.domain.model.ProductEntity;
import kr.hhplus.be.server.user.usecase.dto.UserRequestDTO;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class ProductStep {

    private static String PATH_URL = "/products";

    public static ProductEntity 상품엔티티_기본값(){

        return ProductEntity.builder()
                            .productName("기본 상품")
                            .price(2000L)
                            .quantity(5L)
                            .build();
    }

    public static List<ProductEntity> 전체상품엔티티_기본값(){
        List<ProductEntity> productList = new ArrayList<>();
        ProductEntity product = ProductEntity.builder()
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
}
