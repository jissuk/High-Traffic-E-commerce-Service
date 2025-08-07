package kr.hhplus.be.server.user.domain.repository;


import kr.hhplus.be.server.user.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(long id);

    User save(User user);

}
