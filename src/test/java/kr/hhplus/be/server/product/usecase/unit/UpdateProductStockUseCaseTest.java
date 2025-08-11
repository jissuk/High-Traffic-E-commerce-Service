//package kr.hhplus.be.server.product.usecase.unit;
//
//
//import kr.hhplus.be.server.order.exception.OrderItemNotFoundException;
//import kr.hhplus.be.server.order.step.OrderStep;
//import kr.hhplus.be.server.order.usecase.reader.OrderItemReader;
//import kr.hhplus.be.server.payment.step.PaymentStep;
//import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
//import kr.hhplus.be.server.product.domain.mapper.ProductMapper;
//import kr.hhplus.be.server.product.domain.model.Product;
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
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@DisplayName("상품 수량 변경 테스트")
//@ExtendWith(MockitoExtension.class)
//public class UpdateProductStockUseCaseTest {
//
//    @InjectMocks
//    private UpdateProductStockUseCase updateProductStockUseCase;
//
//    @Mock
//    private ProductReader productReader;
//    @Mock
//    private OrderItemReader orderItemReader;
//    @Mock
//    private ProductRepository productRepository;
//
//    @Spy
//    private ProductMapper productMapper;
//
//    @Nested
//    @DisplayName("상품 수량 변경 성공 케이스")
//    class success{
//
//        @Test
//        @DisplayName("상품과 주문이 존재할 경우 상품의 수량을 변경한다.")
//        void 상품수량변경(){
//            // given
//            PaymentCommand command = PaymentStep.결제커맨드_기본값();
//            when(productReader.findProductOrThrow(command.productId())).thenReturn(ProductStep.상품_기본값());
//            when(orderItemReader.findOrderItemOrThrow(command.orderItemId())).thenReturn(OrderStep.주문상세_기본값());
//
//            // when
//            updateProductStockUseCase.execute(command);
//
//            // then
//            verify(productRepository).save(any(Product.class));
//        }
//    }
//
//    @Nested
//    @DisplayName("상품 수량 변경 실패 케이스")
//    class fail{
//
//        @Test
//        @DisplayName("존재하지 않는 상품일 경우 ProductNotFoundException이 발생한다.")
//        void 상품수량변경_존재하지않는_상품일_경우(){
//            // given
//            PaymentCommand command = PaymentStep.결제커맨드_기본값();
//            when(productReader.findProductOrThrow(command.productId())).thenThrow(new ProductNotFoundException());
//
//            // when & then
//            assertThatThrownBy(() -> updateProductStockUseCase.execute(command))
//                    .isInstanceOf(ProductNotFoundException.class);
//        }
//
//        @Test
//        @DisplayName("존재하지 않는 주문일 경우 OrderItemNotFoundException 발생한다.")
//        void 상품수량변경_존재하지않는_주문일_경우(){
//            // given
//            PaymentCommand command = PaymentStep.결제커맨드_기본값();
//            when(productReader.findProductOrThrow(command.productId())).thenReturn(ProductStep.상품_기본값());
//            when(orderItemReader.findOrderItemOrThrow(command.orderItemId())).thenThrow(new OrderItemNotFoundException());
//
//            // when & then
//            assertThatThrownBy(() -> updateProductStockUseCase.execute(command))
//                    .isInstanceOf(OrderItemNotFoundException.class);
//        }
//    }
//}
