package kr.hhplus.be.server.user.domain.repository;

import kr.hhplus.be.server.user.domain.model.UserEntity;

public interface UserRepository {

    UserEntity findById(long id);

    UserEntity save(UserEntity user);

}
