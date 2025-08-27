package kr.hhplus.be.server.user.event;

import kr.hhplus.be.server.user.domain.model.PointHistoryType;

import java.time.LocalDateTime;

public record PointChargeEvent(
        long userId,
        long point,
        PointHistoryType pointHistoryType,
        LocalDateTime localDateTime
) {
    public static PointChargeEvent from(long userId, long point) {
        return new PointChargeEvent(userId, point, PointHistoryType.CHARGE, LocalDateTime.now());
    }
}