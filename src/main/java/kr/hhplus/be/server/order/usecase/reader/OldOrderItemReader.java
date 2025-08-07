//package kr.hhplus.be.server.order.usecase.reader;
//
//import kr.hhplus.be.server.order.domain.model.OrderItem;
//import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
//import kr.hhplus.be.server.order.exception.OrderItemNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class OldOrderItemReader {
//    private final OrderItemRepository orderItemRepository;
//
//    public OrderItem findOrderItemOrThrow(long id){
//        return orderItemRepository.findById(id).orElseThrow(OrderItemNotFoundException::new);
//    }
//}
//
