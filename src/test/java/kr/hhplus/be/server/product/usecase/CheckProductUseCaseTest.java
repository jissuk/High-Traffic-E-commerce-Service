package kr.hhplus.be.server.product.usecase;

import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.order.usecase.dto.OrderItemRequestDTO;
import kr.hhplus.be.server.product.domain.mapper.ProductMapper;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@DisplayName("상품 수량 확인 테스트")
@ExtendWith(MockitoExtension.class)
public class CheckProductUseCaseTest {

    private CheckProductUseCase checkProductUseCase;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
        checkProductUseCase = new CheckProductUseCase(
                productRepository,
                productMapper
        );
    }

    @Nested
    @DisplayName("성공 케이스")
    class sucess{
        @Test
        void 상품수량확인(){
            // given
            OrderItemRequestDTO request = OrderStep.기본주문상세요청생성();
            when(productRepository.findById(request.getProductId())).thenReturn(ProductStep.기본상품엔티티생성());

            // when & then
            assertDoesNotThrow(() -> checkProductUseCase.execute(request));
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class fail{
        @Test
        void 상품수량확인_존재하지않는_상품일_경우(){
            // given
            OrderItemRequestDTO request = OrderStep.기본주문상세요청생성();
            when(productRepository.findById(request.getProductId())).thenReturn(null);


            // when & then
            assertThatThrownBy(() -> checkProductUseCase.execute(request))
                    .isInstanceOf(ProductNotFoundException.class);
        }
    }

}
