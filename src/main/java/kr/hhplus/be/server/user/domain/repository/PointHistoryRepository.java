package kr.hhplus.be.server.user.domain.repository;


import kr.hhplus.be.server.user.domain.model.PointHistoryEntity;

public interface PointHistoryRepository {

    PointHistoryEntity save(PointHistoryEntity pointHistory);
}
