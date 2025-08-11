package kr.hhplus.be.server.product.usecase.unit;


import kr.hhplus.be.server.product.domain.mapper.ProductRseponseMapper;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import kr.hhplus.be.server.product.step.ProductStep;
import kr.hhplus.be.server.product.usecase.GetProductUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@DisplayName("상품 조회 테스트")
@ExtendWith(MockitoExtension.class)
public class GetProductUseCaseTest {

    @InjectMocks
    private GetProductUseCase getProductUseCase;

    @Mock
    private ProductRepository productRepository;

    @Spy
    private ProductRseponseMapper productRseponseMapper;


    @Nested
    @DisplayName("상품 조회 성공 케이스")
    class success{

        @Test
        @DisplayName("상품이 존재할 경우 상품 조회 시 예외가 발생하지 않는다.")
        void 상품조회(){
            // given
            long  productId = 1L;
            when(productRepository.findById(productId)).thenReturn(Optional.of(ProductStep.상품_기본값()));

            // when
            getProductUseCase.execute(productId);

            // then
            assertDoesNotThrow(() -> getProductUseCase.execute(productId));
        }
    }

    @Nested
    @DisplayName("상품 조회 실패 케이스")
    class fail{

        @Test
        @DisplayName("존재하지 않는 상품일 경우 ProductNotFoundException이 발생한다.")
        void 상품조회_존재하지않는_상품일_경우() {
            // given
            long productId = 1L;
            when(productRepository.findById(productId)).thenThrow(new ProductNotFoundException());

            // when & then
            assertThatThrownBy(() -> getProductUseCase.execute(productId))
                    .isInstanceOf(ProductNotFoundException.class);
        }
    }
}
