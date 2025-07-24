package kr.hhplus.be.server.user.usecase.dto;

import lombok.*;

@Getter
@Setter
public class UserResponseDTO {

    long userId;
    long point;


    public UserResponseDTO() {
    }

    public UserResponseDTO(long userId, long point) {
        this.userId = userId;
        this.point = point;
    }

    @Override
    public String toString() {
        return "UserResponseDTO{" +
                "userId=" + userId +
                ", point=" + point +
                '}';
    }
}
