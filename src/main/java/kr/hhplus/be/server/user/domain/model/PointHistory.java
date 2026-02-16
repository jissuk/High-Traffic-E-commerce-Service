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
public class PointHistory {
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

    public static PointHistory charge(User user) {
        return PointHistory.builder()
                .point(user.getPoint())
                .pointHistoryType(PointHistoryType.CHARGE)
                .createdAt(LocalDateTime.now())
                .userId(user.getId())
                .build();
    }

    public static PointHistory use(User user) {
        return PointHistory.builder()
                .point(user.getPoint())
                .pointHistoryType(PointHistoryType.USE)
                .createdAt(LocalDateTime.now())
                .userId(user.getId())
                .build();
    }

}
