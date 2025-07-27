package kr.hhplus.be.server.product.usecase;


import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.exception.OrderItemNotFoundException;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.payment.step.PaymentStep;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequestDTO;
import kr.hhplus.be.server.product.domain.mapper.ProductMapper;
import kr.hhplus.be.server.product.domain.model.ProductEntity;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import kr.hhplus.be.server.product.step.ProductStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("상품 수량 변경 테스트")
@ExtendWith(MockitoExtension.class)
public class UpdateProductStockUseCaseTest {

    @InjectMocks
    private UpdateProductStockUseCase updateProductStockUseCase;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderItemRepository orderItemRepository;

    @BeforeEach
    void setUp() {
        ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
        updateProductStockUseCase = new UpdateProductStockUseCase(
                productRepository,
                orderItemRepository,
                productMapper
        );
    }

    @Nested
    @DisplayName("상품 수량 변경 성공 케이스")
    class success{

        @Test
        @DisplayName("상품과 주문이 존재할 경우 상품의 수량을 변경한다.")
        void 상품수량변경(){
            // given
            PaymentRequestDTO request = PaymentStep.기본결제요청생성();
            when(productRepository.findById(request.getProductId())).thenReturn(ProductStep.기본상품엔티티생성());
            when(orderItemRepository.findById(request.getOrderItemId())).thenReturn(OrderStep.기본주문상세엔티티생성());

            // when
            updateProductStockUseCase.execute(request);

            // then
            verify(productRepository).update(any(ProductEntity.class));
        }
    }

    @Nested
    @DisplayName("상품 수량 변경 실패 케이스")
    class fail{

        @Test
        @DisplayName("존재하지 않는 상품일 경우 ProductNotFoundException이 발생한다.")
        void 상품수량변경_존재하지않는_상품일_경우(){
            // given
            PaymentRequestDTO request = PaymentStep.기본결제요청생성();
            when(productRepository.findById(request.getProductId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> updateProductStockUseCase.execute(request))
                    .isInstanceOf(ProductNotFoundException.class);
        }

        @Test
        @DisplayName("존재하지 않는 주문일 경우 OrderItemNotFoundException 발생한다.")
        void 상품수량변경_존재하지않는_주문일_경우(){
            // given
            PaymentRequestDTO request = PaymentStep.기본결제요청생성();
            when(productRepository.findById(request.getProductId())).thenReturn(ProductStep.기본상품엔티티생성());
            when(orderItemRepository.findById(request.getOrderItemId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> updateProductStockUseCase.execute(request))
                    .isInstanceOf(OrderItemNotFoundException.class);
        }
    }
}
