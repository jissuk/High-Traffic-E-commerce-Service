package kr.hhplus.be.server.user.infrastructure;

import kr.hhplus.be.server.user.domain.model.PointHistory;
import kr.hhplus.be.server.user.domain.repository.PointHistoryRepository;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaPointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

    private final JpaPointHistoryRepository jpaPointHistoryRepository;

    @Override
    public PointHistory save(PointHistory pointHistory) {
        return jpaPointHistoryRepository.save(pointHistory);
    }
}
