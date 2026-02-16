package kr.hhplus.be.server.user.domain.repository;

import kr.hhplus.be.server.user.domain.model.User;

public interface UserRepository {
    User findById(long id);
    User save(User user);
}
