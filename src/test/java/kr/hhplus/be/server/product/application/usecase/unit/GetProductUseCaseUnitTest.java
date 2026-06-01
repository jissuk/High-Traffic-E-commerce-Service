package kr.hhplus.be.server.product.application.usecase.unit;

import kr.hhplus.be.server.product.domain.mapper.ProductResponseMapper;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import kr.hhplus.be.server.product.application.usecase.GetProductUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@DisplayName("상품 조회 테스트")
@ExtendWith(MockitoExtension.class)
public class GetProductUseCaseUnitTest {
    @InjectMocks
    private GetProductUseCase getProductUseCase;

    @Mock
    private ProductRepository productRepository;

    @Nested
    @DisplayName("상품 조회 실패 케이스")
    class fail{
        @Test
        @DisplayName("존재하지 않는 상품일 경우 ProductNotFoundException이 발생한다.")
        void 상품조회_존재하지않는_상품일_경우() {
            // given
            long productId = Integer.MAX_VALUE;
            when(productRepository.findById(productId)).thenThrow(new ProductNotFoundException());

            // when & then
            assertThatThrownBy(() -> getProductUseCase.execute(productId))
                    .isInstanceOf(ProductNotFoundException.class);
        }
    }
}