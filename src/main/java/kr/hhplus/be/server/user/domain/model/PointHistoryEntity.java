package kr.hhplus.be.server.user.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;



@Entity
@Table(name = "POINT_HISTORY")
@Getter
@Setter
@Builder
public class PointHistoryEntity {

    @Id
    @Column
    private long id;

    @Column
    private long point;

    @Column
    private PointHistoryType pointHistoryType;

    @Column
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

    public PointHistoryEntity() {
    }

    public PointHistoryEntity(long id, long point, PointHistoryType pointHistoryType, LocalDateTime createdAt, UserEntity user) {
        this.id = id;
        this.point = point;
        this.pointHistoryType = pointHistoryType;
        this.createdAt = createdAt;
        this.user = user;
    }

}
