package kr.hhplus.be.server.user.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.usecase.command.UserCommand;
import kr.hhplus.be.server.user.usecase.dto.UserRequestDTO;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class UserStep {

    private static String PATH_URL = "/user";

    public static UserEntity 유저엔티티_기본값(){
        return UserEntity.builder()
                .point(10000L)
                .build();
    }

    public static UserCommand 유저커맨드_기본값() {
        return new UserCommand(1L, 10000L);
    }

    public static ResultActions 유저조회요청(MockMvc mockMvc, long userId) throws Exception {
        return mockMvc.perform(get(PATH_URL + "/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    public static ResultActions 유저포인트충전요청(MockMvc mockMvc, ObjectMapper objectMapper, UserRequestDTO request) throws Exception {
        return mockMvc.perform(post("/user/chargePoint")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print());
    }

}
