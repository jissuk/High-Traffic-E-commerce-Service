package kr.hhplus.be.server.coupon.presentation.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.coupon.application.usecase.IssueCouponUseCase;
import kr.hhplus.be.server.coupon.presentation.CouponController;
import kr.hhplus.be.server.coupon.presentation.dto.UserCouponRequest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CouponController.class)
public class CouponControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private IssueCouponUseCase issueCouponUseCase;

    @Test
    @DisplayName("요청 데이터가 정상적일 경우 유저 쿠폰을 등록한다")
    void 유저쿠폰등록() throws Exception {
        // given
        Long userId = 1L;
        Long couponId = 1L;
        UserCouponRequest request = UserCouponRequest.builder()
                .userId(userId)
                .couponId(couponId)
                .build();
        doNothing().when(issueCouponUseCase).execute(any());

        // when
        ResultActions result = mockMvc.perform(post("/coupons/issue")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isNoContent());

        // verify
        verify(issueCouponUseCase).execute(any());
    }
}
