package kr.hhplus.be.server.user.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PointHistory {

    private long id;
    private long point;
    private PointHistoryType pointHistoryType;
    private LocalDateTime createdAt;

    public static PointHistory charge(User user) {
        return PointHistory.builder()
                .point(user.getPoint())
                .pointHistoryType(PointHistoryType.CHARGE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static PointHistory use(User user) {
        return PointHistory.builder()
                            .point(user.getPoint())
                            .pointHistoryType(PointHistoryType.USE)
                            .createdAt(LocalDateTime.now())
                            .build();
    }

}
