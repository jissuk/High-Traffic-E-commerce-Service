package kr.hhplus.be.server.product.usecase.unit;

import kr.hhplus.be.server.product.domain.mapper.ProductResponseMapper;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.step.ProductStep;
import kr.hhplus.be.server.product.usecase.GetAllProductUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@DisplayName("전체 상품 조회 테스트")
@ExtendWith(MockitoExtension.class)
public class GetAllProductUseCaseTest {

    @InjectMocks
    private GetAllProductUseCase getAllProductUseCase;
    @Mock
    private ProductRepository productRepository;
    @Spy
    private ProductResponseMapper productResponseMapper;

    @Nested
    @DisplayName("전체 상품 조회 성공 케이스")
    class success{
        @Test
        @DisplayName("상품이 존재 시 모든 상품 조회 시 예외가 발생하지 않는다.")
        void 전체상품조회 (){
            // given
            when(productRepository.findAll()).thenReturn(ProductStep.defaultAllProduct());
            // when
            getAllProductUseCase.execute();
            // then
            assertDoesNotThrow(() -> getAllProductUseCase.execute());
        }
    }
}
