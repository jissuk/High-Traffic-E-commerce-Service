package kr.hhplus.be.server.product.usecase;

import kr.hhplus.be.server.product.domain.mapper.ProductMapper;
import kr.hhplus.be.server.product.domain.mapper.ProductRseponseMapper;
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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@DisplayName("전체 상품 조회 테스트")
@ExtendWith(MockitoExtension.class)
public class GetAllProductUseCaseTest {

    private GetAllProductUseCase getAllProductUseCase;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
        ProductRseponseMapper productRseponseMapper = Mappers.getMapper(ProductRseponseMapper.class);
        getAllProductUseCase = new GetAllProductUseCase(
                productRepository,
                productMapper,
                productRseponseMapper
        );
    }

    @Nested
    @DisplayName("전체 상품 조회 성공 케이스")
    class success{

        @Test
        @DisplayName("상품이 존재 시 모든 상품 조회 시 예외가 발생하지 않는다.")
        void 전체상품조회 (){
            // given
            when(productRepository.findAll()).thenReturn(ProductStep.기본전체상품엔티티생성());
            // when
            getAllProductUseCase.execute();
            // then
            assertDoesNotThrow(() -> getAllProductUseCase.execute());
        }
    }

    @Nested
    @DisplayName("전체 상품 조회 실패 케이스")
    class fail{

        @Test
        @DisplayName("상품이 존재하지 않을 경우 ProductNotFoundException이 발생한다.")
        void 전체상품조회_존재하지않는_상품일_경우(){
            // given
            when(productRepository.findAll()).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> getAllProductUseCase.execute())
                    .isInstanceOf(ProductNotFoundException.class);
        }
    }
}
