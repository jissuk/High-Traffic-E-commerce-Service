package kr.hhplus.be.server.user.infrastructure.jpa;

import kr.hhplus.be.server.user.domain.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findById(long id);
}
