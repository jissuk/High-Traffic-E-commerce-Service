package kr.hhplus.be.server.user.domain.repository;

import kr.hhplus.be.server.user.domain.model.UserEntity;

public interface UserRepository {

    UserEntity save(UserEntity user);

    UserEntity findById(long id);

    UserEntity update(UserEntity user);

    void clear();
}
