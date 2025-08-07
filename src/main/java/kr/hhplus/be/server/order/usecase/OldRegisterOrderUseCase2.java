//package kr.hhplus.be.server.order.usecase;
//
//import kr.hhplus.be.server.common.annotation.UseCase;
//import kr.hhplus.be.server.order.domain.model.Order;
//import kr.hhplus.be.server.user.domain.model.User;
//import kr.hhplus.be.server.order.domain.repository.OrderRepository;
//import kr.hhplus.be.server.user.usecase.reader.UserReader;
//import lombok.RequiredArgsConstructor;
//
//@UseCase
//@RequiredArgsConstructor
//public class RegisterOrderUseCase2 {
//
//    private final UserReader userReader;
//
//    private final OrderRepository orderRepositroy;
//
//    public void execute(long userId) {
//        User user = userReader.findUserOrThrow(userId);
//        Order order = Order.createBeforeOrder(user);
//
//        orderRepositroy.save(order);
//    }
//}
