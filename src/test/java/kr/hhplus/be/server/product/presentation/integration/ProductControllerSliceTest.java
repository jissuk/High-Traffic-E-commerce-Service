package kr.hhplus.be.server.product.presentation.integration;

import kr.hhplus.be.server.product.application.usecase.GetAllProductUseCase;
import kr.hhplus.be.server.product.application.usecase.GetPopularProductUseCase;
import kr.hhplus.be.server.product.application.usecase.GetProductUseCase;
import kr.hhplus.be.server.product.presentation.ProductController;
import kr.hhplus.be.server.product.presentation.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private GetProductUseCase getProductUseCase;
    @MockitoBean
    private GetAllProductUseCase getAllProductUseCase;
    @MockitoBean
    private GetPopularProductUseCase getPopularProductUseCase;

    @Test
    @DisplayName("요청 데이터가 정상적일 경우 단건의 상품을 조회한다.")
    void 상품조회() throws Exception {
        // given
        long productId = 1L;
        ProductResponse productResponse = ProductResponse.builder()
                                                            .id(productId)
                                                            .productName("기본상품")
                                                            .price(3000L)
                                                            .quantity(10L)
                                                            .build();
        given(getProductUseCase.execute(anyLong())).willReturn(productResponse);

        // when
        ResultActions result = mockMvc.perform(get("/products" + "/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        result.andExpect(status().isOk());

        //verify
        verify(getProductUseCase).execute(anyLong());
    }

    @Test
    @DisplayName("전체 상품을 조회한다.")
    void 전체상품조회() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        result.andExpect(status().isOk());

        //verify
        verify(getAllProductUseCase).execute();
    }

    @Test
    @DisplayName("인기 판매 상품을 조회한다.")
    void 인기판매상품조회() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/products/popular")
                                        .contentType(MediaType.APPLICATION_JSON))
                                        .andDo(print());

        // then
        result.andExpect(status().isOk());

        //verify
        verify(getPopularProductUseCase).execute();
    }

    private ProductResponse productResponse(long productId){
        return ProductResponse.builder()
                .id(productId)
                .productName("기본상품")
                .price(3000L)
                .quantity(10L)
                .build();
    }
}
