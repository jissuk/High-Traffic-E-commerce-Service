package kr.hhplus.be.server.user.usecase.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Builder
public record UserRequest(
    @NotNull(message = "유저 ID는 필수입니다.")
    @Min( value = 1, message ="유저 ID는 1 이상이어야 합니다.")
    Long userId,

    @Min( value = 0, message ="포인트는 0 이상이어야 합니다.")
    Long point
){}

