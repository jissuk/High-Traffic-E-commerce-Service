package kr.hhplus.be.server.user.usecase;


import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.user.domain.mapper.PointHistoryMapper;
import kr.hhplus.be.server.user.domain.mapper.UserMapper;
import kr.hhplus.be.server.user.domain.mapper.UserResponseMapper;
import kr.hhplus.be.server.user.domain.model.PointHistory;
import kr.hhplus.be.server.user.domain.model.PointHistoryEntity;
import kr.hhplus.be.server.user.domain.model.User;
import kr.hhplus.be.server.user.domain.repository.PointHistoryRepository;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.exception.UserNotFoundException;
import kr.hhplus.be.server.user.usecase.dto.UserRequestDTO;
import kr.hhplus.be.server.user.usecase.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ChargePointUseCase {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final UserMapper userMapper;
    private final PointHistoryMapper pointHistoryMapper;
    private final UserResponseMapper userResponseMapper;

    public UserResponseDTO execute(UserRequestDTO request) {

        UserEntity userEntity = findUserOrThrow(request.getUserId());
        User user = userMapper.toDomain(userEntity);

        user.charegePoint(request.getPoint());
        PointHistory pointHistory = PointHistory.charge(user);

        UserEntity updateUser = userMapper.toEntity(user);
        PointHistoryEntity saveHistory = pointHistoryMapper.toEntity(pointHistory);

        userRepository.update(updateUser);
        pointHistoryRepository.save(saveHistory);

        return userResponseMapper.toDto(user);
    }

    private UserEntity findUserOrThrow(long id) {
        UserEntity user = userRepository.findById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }
}
