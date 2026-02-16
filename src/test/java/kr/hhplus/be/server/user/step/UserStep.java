package kr.hhplus.be.server.user.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.usecase.command.UserCommand;
import kr.hhplus.be.server.user.usecase.dto.UserRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class UserStep {

    private static String PATH_URL = "/user";

    public static UserRequest defaultUserRequest(){
        return UserRequest.builder()
                                .userId(1L)
                                .point(3000L)
                                .build();
    }

    public static UserRequest userRequestWithUserId(long userId){
        return UserRequest.builder()
                .userId(userId)
                .point(3000L)
                .build();
    }

    public static UserCommand defaultUserCommand() {
        return new UserCommand(1L, 3000L);
    }

    public static User defualtUser(){
        return User.builder()
                    .point(10000L)
                    .build();
    }

    public static User defualtUserEntity(){
        return User.builder()
                .point(40000L)
                .build();
    }

    public static ResultActions chargePointRequest(MockMvc mockMvc, ObjectMapper objectMapper, UserRequest request) throws Exception {
        return mockMvc.perform(post(PATH_URL+ "/chargePoint")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print());
    }

}
