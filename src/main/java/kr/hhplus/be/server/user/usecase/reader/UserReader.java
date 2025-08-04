package kr.hhplus.be.server.user.usecase.reader;


import kr.hhplus.be.server.user.domain.mapper.UserMapper;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.exception.UserNotFoundException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReader {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User findUserOrThrow(long id){
        UserEntity userEntity = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userMapper.toDomain(userEntity);
    }
}
