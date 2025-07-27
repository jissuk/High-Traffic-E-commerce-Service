package kr.hhplus.be.server.coupon.usecase.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserCouponRequestDTO {
    
    @NotNull(message = "유저 ID는 필수입니다.")
    @Min( value = 1, message ="유저 ID는 1 이상이어야 합니다.")
    Long userId;

    @NotNull(message = "쿠폰 ID는 필수입니다.")
    @Min( value = 1, message ="쿠폰 ID는 1 이상이어야 합니다.")
    Long couponId;

    public UserCouponRequestDTO() {
    }

    public UserCouponRequestDTO(Long userId, Long couponId) {
        this.userId = userId;
        this.couponId = couponId;
    }

    @Override
    public String toString() {
        return "UserCouponRequestDTO{" +
                "userId=" + userId +
                ", couponId=" + couponId +
                '}';
    }
}