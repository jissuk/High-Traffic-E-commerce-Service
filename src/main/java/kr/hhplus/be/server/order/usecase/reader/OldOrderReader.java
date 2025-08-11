//package kr.hhplus.be.server.order.usecase.reader;
//
//import kr.hhplus.be.server.order.domain.model.Order;
//import kr.hhplus.be.server.order.domain.repository.OrderRepository;
//import kr.hhplus.be.server.order.exception.OrderNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class OldOrderReader {
//    private final OrderRepository orderRepository;
//
//    public Order findOrderOrThrow(long id){
//        return orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
//    }
//}
