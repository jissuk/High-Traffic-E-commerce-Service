package kr.hhplus.be.server.user.infrastructure;

import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.exception.UserNotFoundException;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public User findById(long id) {
        return jpaUserRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }
    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }
}
