package kr.hhplus.be.server.user.domain.model;

import kr.hhplus.be.server.user.exception.InsufficientPointBalanceException;
import kr.hhplus.be.server.user.exception.InvalidPointAmountException;
import kr.hhplus.be.server.user.exception.PointLimitExceededException;
import kr.hhplus.be.server.user.usecase.command.UserCommand;
import kr.hhplus.be.server.user.usecase.dto.UserRequestDTO;
import lombok.*;

@Getter
@Setter
@Builder
public class User {

    /*
     * 정책 정의
     * 1. 포인트 잔고는 100,000원을 넘어갈 수 없습니다
     *    (잔고 포인트와 충전하려는 포인트의 합이 10만원을 넘어갈 경우 최대 잔고 제한으로 인한 실패처리)
     * 2. 포인트 잔고는 0원 밑으로 내려갈 수 없습니다.
     *    (잔고 포인트보다 결제 포인트가 더 클 경우 잔고 부족으로 인한 실패처리)
     * 3. 0원 이하의 포인트 충전은 불가능합니다.
     * */

    long userId;
    long point;

    public void charegePoint(long amount) {
        this.point += amount;

        if (this.point < 0) {
            throw new InvalidPointAmountException();
        }

        if (100000 < this.point) {
            throw new PointLimitExceededException();
        }
    }

    public void deductPoint(long amount) {
        this.point -= amount;

        if (this.point < 0) {
            throw new InsufficientPointBalanceException();
        }

    }

    public static User createBeforeUser(UserCommand command) {
        return User.builder()
                .point(command.point())
                .build();
    }

    public User() {
    }

    public User(long userId, long point) {
        this.userId = userId;
        this.point = point;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", point=" + point +
                '}';
    }
}