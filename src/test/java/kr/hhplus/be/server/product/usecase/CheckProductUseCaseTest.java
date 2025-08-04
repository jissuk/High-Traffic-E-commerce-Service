package kr.hhplus.be.server.product.usecase;

import kr.hhplus.be.server.order.step.OrderStep;
import kr.hhplus.be.server.order.usecase.command.OrderItemCommand;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@DisplayName("상품 수량 확인 테스트")
@ExtendWith(MockitoExtension.class)
public class CheckProductUseCaseTest {
    @InjectMocks
    private CheckProductUseCase checkProductUseCase;

    @Mock
    private ProductRepository productRepository;

    @Spy
    private ProductMapper productMapper;

    @Nested
    @DisplayName("상품 수량 확인 성공 케이스")
    class sucess{

        @Test
        @DisplayName("주문한 수량이 부족하지 않으면 예외를 발생시키지 않는다.")
        void 상품수량확인(){
            // given
            OrderItemCommand command = OrderStep.주문상세커맨드_기본값();
            when(productRepository.findById(command.productId())).thenReturn(ProductStep.상품엔티티_기본값());

            // when & then
            assertDoesNotThrow(() -> checkProductUseCase.execute(command));
        }
    }

    @Nested
    @DisplayName("상품 수량 확인 실패 케이스")
    class fail{

        @Test
        @DisplayName("존재하지 않는 상품일 경우 ProductNotFoundException이 발생한다.")
        void 상품수량확인_존재하지않는_상품일_경우(){
            // given
            OrderItemCommand command = OrderStep.주문상세커맨드_기본값();
            when(productRepository.findById(command.productId())).thenReturn(null);


            // when & then
            assertThatThrownBy(() -> checkProductUseCase.execute(command))
                    .isInstanceOf(ProductNotFoundException.class);
        }
    }

}
