package kr.hhplus.be.server.user.infrastructure.jpa;

import kr.hhplus.be.server.user.domain.model.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPointHistoryRepository extends JpaRepository<PointHistory, Long> {
}
