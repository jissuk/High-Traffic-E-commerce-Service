package kr.hhplus.be.server.order.presentation.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.order.application.usecase.RegisterOrderUseCase;
import kr.hhplus.be.server.order.presentation.OrderController;
import kr.hhplus.be.server.order.presentation.dto.OrderRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private RegisterOrderUseCase registerOrderUseCase;

    @Test
    @DisplayName("요청 데이터가 정상적일 경우 주문을 등록한다.")
    void 주문() throws Exception {
        // given
        long userId = 1L;
        long productId = 1L;
        long userCouponId = 1L;
        long quantity = 1L;
        long point = 3000L;
        OrderRequest request = OrderRequest.builder()
                                            .userId(userId)
                                            .productId(productId)
                                            .userCouponId(userCouponId)
                                            .quantity(quantity)
                                            .point(point)
                                            .build();
        doNothing().when(registerOrderUseCase).execute(any());

        // when
        ResultActions result = mockMvc.perform(post("/orders")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        result.andExpect(status().isNoContent());

        // verify
        verify(registerOrderUseCase).execute(any());
    }
}
