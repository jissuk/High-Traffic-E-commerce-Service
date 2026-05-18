package kr.hhplus.be.server.user.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
import kr.hhplus.be.server.user.presentation.dto.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@DisplayName("유저 관련 테스트")
public class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JpaUserRepository userRepository;

    @BeforeEach
    void setUp() {
        clearTestData();
        initTestData();
    }

    private User savedUser;

    private void initTestData() {
        User user = User.builder()
                .point(40000L)
                .build();
        savedUser = userRepository.save(user);
    }

    @Nested
    @DisplayName("유저 포인트 충전 성공 케이스")
    class success{
        @Test
        @DisplayName("요청 데이터가 정상적일 경우 포인트를 충전한다.")
        void 포인트충전() throws Exception {
            // given
            long userId = savedUser.getId();
            UserRequest request = UserRequest.builder()
                                                .userId(userId)
                                                .point(3000L)
                                                .build();

            // when
            ResultActions result = mockMvc.perform(post("/user/chargePoint")
                                            .content(objectMapper.writeValueAsString(request))
                                            .contentType(MediaType.APPLICATION_JSON))
                                    .andDo(print());

            // then
            result.andExpect(jsonPath("$.status").value(201));
        }
    }

    @Nested
    @DisplayName("유저 포인트 충전 실패 케이스")
    class fail{
        @Test
        @DisplayName("존재하지 않는 유저일 경우 UserNotFoundException이 발생한다.")
        void 포인트충전() throws Exception {
            // given
            long userId = Integer.MAX_VALUE;
            UserRequest request = UserRequest.builder()
                                        .userId(userId)
                                        .point(3000L)
                                        .build();

            // when
            ResultActions result = mockMvc.perform(post("/user/chargePoint")
                                            .content(objectMapper.writeValueAsString(request))
                                            .contentType(MediaType.APPLICATION_JSON))
                                    .andDo(print());

            // then
            result.andExpect(jsonPath("$.code").value("UserNotFound"));
        }
    }

    private void clearTestData() {
        userRepository.deleteAll();

        savedUser = null;
    }
}