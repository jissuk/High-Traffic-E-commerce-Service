package kr.hhplus.be.server.user.domain.model;

import kr.hhplus.be.server.user.exception.InsufficientPointBalanceException;
import kr.hhplus.be.server.user.exception.InvalidPointAmountException;
import kr.hhplus.be.server.user.exception.PointLimitExceededException;
import kr.hhplus.be.server.user.usecase.dto.UserRequestDTO;
import lombok.*;

@Getter
@Setter
@Builder
public class UserEntity {

    long userId;
    long point;

    public UserEntity() {
    }

    public UserEntity(long userId, long point) {
        this.userId = userId;
        this.point = point;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", point=" + point +
                '}';
    }
}
