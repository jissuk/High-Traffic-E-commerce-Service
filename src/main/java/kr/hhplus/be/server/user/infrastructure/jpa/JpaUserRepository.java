package kr.hhplus.be.server.user.infrastructure.jpa;

import kr.hhplus.be.server.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(long id);
}
