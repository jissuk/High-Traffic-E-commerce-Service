package kr.hhplus.be.server.user.infrastructure.jpa;

import kr.hhplus.be.server.user.domain.model.PointHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPointHistoryRepository extends JpaRepository<PointHistoryEntity, Long> {
}
