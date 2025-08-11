package kr.hhplus.be.server.user.infrastructure;


import kr.hhplus.be.server.user.domain.mapper.PointHistoryMapper;
import kr.hhplus.be.server.user.domain.model.PointHistory;
import kr.hhplus.be.server.user.domain.model.PointHistoryEntity;
import kr.hhplus.be.server.user.domain.model.PointHistoryType;
import kr.hhplus.be.server.user.domain.repository.PointHistoryRepository;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaPointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

    private final JpaPointHistoryRepository jpaPointHistoryRepository;
    private final PointHistoryMapper pointHistoryMapper;


    @Override
    public PointHistory save(PointHistory pointHistory) {
        return pointHistoryMapper.toDomain(
                jpaPointHistoryRepository.save(pointHistoryMapper.toEntity(pointHistory))
        );
    }
}
