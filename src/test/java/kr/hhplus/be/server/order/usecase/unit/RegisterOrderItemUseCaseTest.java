//package kr.hhplus.be.server.order.usecase.unit;
//
//import kr.hhplus.be.server.order.domain.mapper.OrderItemMapper;
//import kr.hhplus.be.server.order.domain.model.OrderItem;
//import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
//import kr.hhplus.be.server.order.domain.repository.OrderRepository;
//import kr.hhplus.be.server.order.exception.OrderNotFoundException;
//import kr.hhplus.be.server.order.step.OrderStep;
//import kr.hhplus.be.server.order.usecase.command.OrderItemCommand;
//import kr.hhplus.be.server.order.usecase.reader.OrderReader;
//import kr.hhplus.be.server.product.domain.repository.ProductRepository;
//import kr.hhplus.be.server.product.exception.ProductNotFoundException;
//import kr.hhplus.be.server.product.step.ProductStep;
//import kr.hhplus.be.server.product.usecase.reader.ProductReader;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@DisplayName("주문상세 등록 테스트")
//@ExtendWith(MockitoExtension.class)
//public class RegisterOrderItemUseCaseTest {
//    @InjectMocks
//    RegisterOrderItemUseCase registerOrderItemUseCase;
//
//    @Mock
//    private OrderReader orderReader;
//    @Mock
//    private ProductReader productReader;
//
//    @Mock
//    private ProductRepository productRepository;
//    @Mock
//    private OrderRepository orderRepositroy;
//    @Mock
//    private OrderItemRepository orderItemRepository;
//    @Spy
//    private OrderItemMapper OrderItemMapper;
//
//    @Nested
//    @DisplayName("주문상세 등록 성공 케이스")
//    class success {
//
//        @Test
//        @DisplayName("주문과 상품이 존재할 경우 주문 상세를 등록한다.")
//        void 주문상세등록(){
//            // given
//            OrderItemCommand command = OrderStep.주문상세커맨드_기본값();
//            when(productReader.findProductOrThrow(command.productId())).thenReturn(ProductStep.상품_기본값());
//            when(orderReader.findOrderOrThrow(command.orderId())).thenReturn(OrderStep.주문_기본값());
//
//            // when
//            registerOrderItemUseCase.execute(command);
//
//            // then
//            verify(orderItemRepository).save(any(OrderItem.class));
//        }
//    }
//
//    @Nested
//    @DisplayName("주문 상세 등록 실패 케이스")
//    class fail{
//
//        @Test
//        @DisplayName("존재하지 않는 상품일 경우 ProductNotFoundException이 발생한다.")
//        void 주문상세등록_존재하지않는_상품일_경우(){
//            // given
//            OrderItemCommand command = OrderStep.주문상세커맨드_기본값();
//            when(productReader.findProductOrThrow(command.productId())).thenThrow(new ProductNotFoundException());
//
//
//
//            // when & then
//            assertThatThrownBy(() -> registerOrderItemUseCase.execute(command))
//                    .isInstanceOf(ProductNotFoundException.class);
//        }
//
//        @Test
//        @DisplayName("존재하지 않는 주문일 경우 OrderNotFoundException이 발생한다.")
//        void 주문상세등록_존재하지않는_주문일_경우(){
//            // given
//            OrderItemCommand command = OrderStep.주문상세커맨드_기본값();
//            when(productReader.findProductOrThrow(command.productId())).thenReturn(ProductStep.상품_기본값());
//            when(orderReader.findOrderOrThrow(command.orderId())).thenThrow(new OrderNotFoundException());
//
//            // when & then
//            assertThatThrownBy(() -> registerOrderItemUseCase.execute(command))
//                    .isInstanceOf(OrderNotFoundException.class);
//        }
//    }
//}