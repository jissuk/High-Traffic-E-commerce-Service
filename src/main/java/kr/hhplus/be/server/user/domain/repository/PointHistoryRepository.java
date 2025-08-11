package kr.hhplus.be.server.user.domain.repository;


import kr.hhplus.be.server.user.domain.model.PointHistory;

public interface PointHistoryRepository {

    PointHistory save(PointHistory pointHistory);
}
