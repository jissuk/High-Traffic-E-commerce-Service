package kr.hhplus.be.server.user.infrastructure.jpa;

import kr.hhplus.be.server.user.domain.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findById(long id);
}
