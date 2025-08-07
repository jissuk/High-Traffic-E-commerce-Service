package kr.hhplus.be.server.user.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.user.domain.model.PointHistoryEntity;
import kr.hhplus.be.server.user.domain.model.PointHistoryType;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.usecase.command.UserCommand;
import kr.hhplus.be.server.user.usecase.dto.UserRequestDTO;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class UserStep {

    private static String PATH_URL = "/user";

    public static UserRequestDTO 유저요청생성_기본값(){
        return UserRequestDTO.builder()
                                .userId(1L)
                                .point(3000L)
                                .build();
    }

    public static UserCommand 유저커맨드_기본값() {
        return new UserCommand(1L, 3000L);
    }

    public static User 유저_기본값(){
        return User.builder()
                    .point(10000L)
                    .build();
    }

    public static UserEntity 유저엔티티_기본값(){
        return UserEntity.builder()
                .point(40000L)
                .build();
    }

    public static PointHistoryEntity 포인트내역엔티티_기본값(UserEntity user){
        return PointHistoryEntity.builder()
                .point(10000L)
                .pointHistoryType(PointHistoryType.CHARGE)
                .createdAt(LocalDateTime.now())
                .userId(user.getId())
                .build();
    }

    public static ResultActions 유저포인트충전요청(MockMvc mockMvc, ObjectMapper objectMapper, UserRequestDTO request) throws Exception {
        return mockMvc.perform(post(PATH_URL+ "/chargePoint")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print());
    }

}
