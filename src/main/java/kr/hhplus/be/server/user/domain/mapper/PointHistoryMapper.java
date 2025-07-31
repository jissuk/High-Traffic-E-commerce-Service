package kr.hhplus.be.server.user.domain.mapper;

import kr.hhplus.be.server.user.domain.model.PointHistory;
import kr.hhplus.be.server.user.domain.model.PointHistoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PointHistoryMapper {
    PointHistory toDomain(PointHistoryEntity pointHistory);
    PointHistoryEntity toEntity(PointHistory pointHistory);
}
