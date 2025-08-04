package kr.hhplus.be.server.user.domain.mapper;

import kr.hhplus.be.server.user.domain.model.PointHistory;
import kr.hhplus.be.server.user.domain.model.PointHistoryEntity;
import org.springframework.stereotype.Component;


@Component
public class PointHistoryMapper {
    public PointHistory toDomain(PointHistoryEntity pointHistory){
        return PointHistory.builder()
                            .id(pointHistory.getId())
                            .point(pointHistory.getPoint())
                            .pointHistoryType(pointHistory.getPointHistoryType())
                            .createdAt(pointHistory.getCreatedAt())
                            .build();
    };
    public PointHistoryEntity toEntity(PointHistory pointHistory){
        return PointHistoryEntity.builder()
                                    .id(pointHistory.getId())
                                    .point(pointHistory.getPoint())
                                    .pointHistoryType(pointHistory.getPointHistoryType())
                                    .createdAt(pointHistory.getCreatedAt())
                                    .build();
    }
}
