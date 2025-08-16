package kr.hhplus.be.server.user.infrastructure;

import kr.hhplus.be.server.user.domain.mapper.UserMapper;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.exception.UserNotFoundException;
import kr.hhplus.be.server.user.infrastructure.jpa.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    @Override
    public User findById(long id) {
        return jpaUserRepository.findById(id)
                .map(userMapper::toDomain)
                .orElseThrow(UserNotFoundException::new);
    }
    @Override
    public User save(User user) {
        return userMapper.toDomain(
                jpaUserRepository.save(userMapper.toEntity(user))
        );
    }
}
