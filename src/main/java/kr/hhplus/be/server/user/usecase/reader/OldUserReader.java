//package kr.hhplus.be.server.user.usecase.reader;
//
//
//import kr.hhplus.be.server.user.domain.model.User;
//import kr.hhplus.be.server.user.domain.repository.UserRepository;
//import kr.hhplus.be.server.user.exception.UserNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class UserReader {
//
//    private final UserRepository userRepository;
//
//    public User findUserOrThrow(long id){
//        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
//    }
//}
