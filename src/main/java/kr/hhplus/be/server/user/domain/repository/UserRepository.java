package kr.hhplus.be.server.user.domain.repository;

import kr.hhplus.be.server.user.domain.model.UserEntity;

import java.util.Optional;

public interface UserRepository {

    Optional<UserEntity> findById(long id);

    UserEntity save(UserEntity user);

}
