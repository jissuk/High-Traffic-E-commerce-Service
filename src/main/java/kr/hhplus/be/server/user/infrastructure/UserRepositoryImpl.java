package kr.hhplus.be.server.user.infrastructure;

import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepositroy) {
        this.jpaUserRepository = jpaUserRepositroy;
    }

    @Override
    public Optional<UserEntity> findById(long id) {
        return jpaUserRepository.findById(id);
    }
    @Override
    public UserEntity save(UserEntity user) {
        return jpaUserRepository.save(user);
    }

}
