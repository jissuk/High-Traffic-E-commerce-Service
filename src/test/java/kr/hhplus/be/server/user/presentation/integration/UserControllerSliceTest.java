package kr.hhplus.be.server.user.presentation.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.user.application.usecase.ChargePointUseCase;
import kr.hhplus.be.server.user.presentation.UserController;
import kr.hhplus.be.server.user.presentation.dto.UserRequest;
import kr.hhplus.be.server.user.presentation.dto.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private ChargePointUseCase chargePointUseCase;

    @Test
    @DisplayName("요청 데이터가 정상적일 경우 포인트를 충전한다.")
    void 포인트충전() throws Exception {
        // given
        UserRequest request = UserRequest.builder()
                .userId(1L)
                .point(3000L)
                .build();
        UserResponse response = UserResponse.builder()
                                    .userId(1L)
                                    .point(3000L)
                                    .build();
        given(chargePointUseCase.execute(any())).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(post("/user/chargePoint")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        result.andExpect(status().isOk());

        // verify
        verify(chargePointUseCase).execute(any());
    }
}
