package kr.hhplus.be.server.user.infrastructure;


import kr.hhplus.be.server.user.domain.model.PointHistoryEntity;
import kr.hhplus.be.server.user.domain.model.PointHistoryType;
import kr.hhplus.be.server.user.domain.repository.PointHistoryRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

    private final Map<Long, PointHistoryEntity> pointHistoryTable = new HashMap<>();

    @Override
    public PointHistoryEntity save(PointHistoryEntity pointHistory) {
        long sequnce = 0;
        sequnce++;
        return pointHistoryTable.put(sequnce, pointHistory);
    }

    @Override
    public void updateType(Long userId, PointHistoryType pointHistoryType) {
        PointHistoryEntity pointHistoryEntity  = pointHistoryTable.get(userId);
        pointHistoryEntity.setPointHistoryType(pointHistoryType);

        pointHistoryTable.put(userId, pointHistoryEntity);
    }
}
