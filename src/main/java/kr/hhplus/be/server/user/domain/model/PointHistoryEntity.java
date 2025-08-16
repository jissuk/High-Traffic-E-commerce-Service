package kr.hhplus.be.server.user.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "POINT_HISTORIES")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PointHistoryEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long point;

    @Column
    private PointHistoryType pointHistoryType;

    @Column
    private LocalDateTime createdAt;

    @Column
    private long userId;

}
