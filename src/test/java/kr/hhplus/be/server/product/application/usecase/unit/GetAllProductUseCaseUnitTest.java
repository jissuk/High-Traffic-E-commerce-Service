package kr.hhplus.be.server.product.application.usecase.unit;

import kr.hhplus.be.server.product.domain.mapper.ProductResponseMapper;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.presentation.dto.ProductResponse;
import kr.hhplus.be.server.product.application.usecase.GetAllProductUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("전체 상품 조회 테스트")
@ExtendWith(MockitoExtension.class)
public class GetAllProductUseCaseUnitTest {

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
        @DisplayName("상품이 존재하면 전체 상품을 조회한다.")
        void 전체상품조회() {

            // given
            List<Product> products = new ArrayList<>();
            Product product = Product.builder()
                    .productName("기본 상품")
                    .price(2000L)
                    .quantity(5L)
                    .build();
            products.add(product);
            when(productRepository.findAll()).thenReturn(products);

            // when
            List<ProductResponse> result =getAllProductUseCase.execute();

            // then
            assertThat(result).hasSize(products.size());
        }
    }
}
