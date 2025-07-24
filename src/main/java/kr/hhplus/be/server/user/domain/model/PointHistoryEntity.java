package kr.hhplus.be.server.user.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PointHistoryEntity {

    long id;
    long point;
    PointHistoryType pointHistoryType;
    LocalDateTime createdAt;

    UserEntity user;

    public PointHistoryEntity() {
    }

    public PointHistoryEntity(long id, long point, PointHistoryType pointHistoryType, LocalDateTime createdAt, UserEntity user) {
        this.id = id;
        this.point = point;
        this.pointHistoryType = pointHistoryType;
        this.createdAt = createdAt;
        this.user = user;
    }

    @Override
    public String toString() {
        return "PointHistoryEntity{" +
                "id=" + id +
                ", point=" + point +
                ", pointHistoryType=" + pointHistoryType +
                ", createdAt=" + createdAt +
                ", user=" + user +
                '}';
    }
}
