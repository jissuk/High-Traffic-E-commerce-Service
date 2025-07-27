package kr.hhplus.be.server.order.usecase;

import kr.hhplus.be.server.order.domain.mapper.OrderHistoryMapper;
import kr.hhplus.be.server.order.domain.mapper.OrderItemMapper;
import kr.hhplus.be.server.order.domain.mapper.OrderMapper;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.domain.repository.OrderRepositroy;
import kr.hhplus.be.server.order.exception.OrderNotFoundException;
import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.order.usecase.dto.OrderItemRequestDTO;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import kr.hhplus.be.server.product.step.ProductStep;
import kr.hhplus.be.server.user.domain.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("주문상세 생성 테스트")
@ExtendWith(MockitoExtension.class)
public class CreateOrderItemUseCaseTest {

    CreateOrderItemUseCase createOrderItemUseCase;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderRepositroy orderRepositroy;
    @Mock
    private OrderItemRepository orderItemRepository;

    @BeforeEach
    void setUp() {
        OrderItemMapper OrderItemMapper = Mappers.getMapper(OrderItemMapper.class);
        createOrderItemUseCase = new CreateOrderItemUseCase(
                productRepository,
                orderRepositroy,
                orderItemRepository,
                OrderItemMapper
        );
    }

    @Nested
    @DisplayName("성공 케이스")
    class success {
        @Test
        void 주문상세생성(){
            // given
            OrderItemRequestDTO request = OrderStep.기본주문상세요청생성();
            when(orderRepositroy.findById(request.getOrderId())).thenReturn(OrderStep.기본주문엔티티생성());
            when(productRepository.findById(request.getProductId())).thenReturn(ProductStep.기본상품엔티티생성());

            // when
            createOrderItemUseCase.execute(request);

            // then
            verify(orderItemRepository).save(any(OrderItemEntity.class));
        }
    }
    
    @Nested
    @DisplayName("실패 케이스")
    class fail{

        @Test
        void 주문상세생성_존재하지않는_상품일_경우(){
            // given
            OrderItemRequestDTO request = OrderStep.기본주문상세요청생성();
            when(productRepository.findById(request.getProductId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> createOrderItemUseCase.execute(request))
                    .isInstanceOf(ProductNotFoundException.class);
        }

        @Test
        void 주문상세생성_존재하지않는_주문일_경우(){
            // given
            OrderItemRequestDTO request = OrderStep.기본주문상세요청생성();
            when(productRepository.findById(request.getProductId())).thenReturn(ProductStep.기본상품엔티티생성());
            when(orderRepositroy.findById(request.getOrderId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> createOrderItemUseCase.execute(request))
                    .isInstanceOf(OrderNotFoundException.class);
        }
    }
}