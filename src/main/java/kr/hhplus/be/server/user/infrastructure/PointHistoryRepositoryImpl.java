package kr.hhplus.be.server.user.infrastructure;


import kr.hhplus.be.server.user.domain.model.PointHistoryEntity;
import kr.hhplus.be.server.user.domain.model.PointHistoryType;
import kr.hhplus.be.server.user.domain.repository.PointHistoryRepository;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaPointHistoryRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

    private final JpaPointHistoryRepository jpaPointHistoryRepository;

    public PointHistoryRepositoryImpl(JpaPointHistoryRepository jpaPointHistoryRepository) {
        this.jpaPointHistoryRepository = jpaPointHistoryRepository;
    }

    @Override
    public PointHistoryEntity save(PointHistoryEntity pointHistory) {
        return jpaPointHistoryRepository.save(pointHistory);
    }
}
