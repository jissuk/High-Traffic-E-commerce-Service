package kr.hhplus.be.server.user.domain.repository;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.user.domain.model.PointHistoryEntity;
import kr.hhplus.be.server.user.domain.model.PointHistoryType;

public interface PointHistoryRepository {

    PointHistoryEntity save(PointHistoryEntity pointHistory);

    void updateType(@NotNull(message ="유저 ID는 필수입니다.") @Min( value = 1, message ="유저 ID는 1 이상이어야 합니다.") Long userId, PointHistoryType pointHistoryType);
}
