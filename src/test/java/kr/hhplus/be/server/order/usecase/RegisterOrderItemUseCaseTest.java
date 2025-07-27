package kr.hhplus.be.server.order.usecase;

import kr.hhplus.be.server.order.domain.mapper.OrderItemMapper;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.domain.repository.OrderRepositroy;
import kr.hhplus.be.server.order.exception.OrderNotFoundException;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.order.usecase.dto.OrderItemRequestDTO;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import kr.hhplus.be.server.product.step.ProductStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("주문상세 등록 테스트")
@ExtendWith(MockitoExtension.class)
public class RegisterOrderItemUseCaseTest {

    RegisterOrderItemUseCase registerOrderItemUseCase;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderRepositroy orderRepositroy;
    @Mock
    private OrderItemRepository orderItemRepository;

    @BeforeEach
    void setUp() {
        OrderItemMapper OrderItemMapper = Mappers.getMapper(OrderItemMapper.class);
        registerOrderItemUseCase = new RegisterOrderItemUseCase(
                productRepository,
                orderRepositroy,
                orderItemRepository,
                OrderItemMapper
        );
    }

    @Nested
    @DisplayName("주문상세 등록 성공 케이스")
    class success {
        
        @Test
        @DisplayName("주문과 상품이 존재할 경우 주문 상세를 등록한다.")
        void 주문상세등록(){
            // given
            OrderItemRequestDTO request = OrderStep.기본주문상세요청생성();
            when(orderRepositroy.findById(request.getOrderId())).thenReturn(OrderStep.기본주문엔티티생성());
            when(productRepository.findById(request.getProductId())).thenReturn(ProductStep.기본상품엔티티생성());

            // when
            registerOrderItemUseCase.execute(request);

            // then
            verify(orderItemRepository).save(any(OrderItemEntity.class));
        }
    }
    
    @Nested
    @DisplayName("주문 상세 등록 실패 케이스")
    class fail{

        @Test
        @DisplayName("존재하지 않는 상품일 경우 ProductNotFoundException이 발생한다.")
        void 주문상세등록_존재하지않는_상품일_경우(){
            // given
            OrderItemRequestDTO request = OrderStep.기본주문상세요청생성();
            when(productRepository.findById(request.getProductId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> registerOrderItemUseCase.execute(request))
                    .isInstanceOf(ProductNotFoundException.class);
        }

        @Test
        @DisplayName("존재하지 않는 주문일 경우 OrderNotFoundException이 발생한다.")
        void 주문상세등록_존재하지않는_주문일_경우(){
            // given
            OrderItemRequestDTO request = OrderStep.기본주문상세요청생성();
            when(productRepository.findById(request.getProductId())).thenReturn(ProductStep.기본상품엔티티생성());
            when(orderRepositroy.findById(request.getOrderId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> registerOrderItemUseCase.execute(request))
                    .isInstanceOf(OrderNotFoundException.class);
        }
    }
}