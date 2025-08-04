package kr.hhplus.be.server.user.usecase.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserResponseDTO {

    long userId;
    long point;
}
