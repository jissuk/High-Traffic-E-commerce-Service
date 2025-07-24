package kr.hhplus.be.server.user.infrastructure;

import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, UserEntity> usersTable = new HashMap<>();

    @Override
    public UserEntity findById(long id) {
        return usersTable.get(id);
    }

    @Override
    public UserEntity update(UserEntity user) {
        return usersTable.put(user.getUserId(), user);
    }

    @Override
    public UserEntity save(UserEntity user) {
        long sequence = 0L;
        sequence++;
        return usersTable.put(sequence, user);
    }

    @Override
    public void clear() {
        usersTable.clear();
    }

}
