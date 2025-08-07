//package kr.hhplus.be.server.product.repository;
//
//
//import kr.hhplus.be.server.product.domain.model.ProductEntity;
//import kr.hhplus.be.server.product.domain.repository.ProductRepository;
//import kr.hhplus.be.server.product.step.ProductStep;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.transaction.annotation.Transactional;
//import org.testcontainers.utility.TestcontainersConfiguration;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Import(TestcontainersConfiguration.class)
//@DisplayName("상품 관련 테스트")
//public class ProductRepositoryTest {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @BeforeEach
//    void setUp() {
//        clearTestData();
//    }
//    private void clearTestData() {
//        jdbcTemplate.execute("TRUNCATE TABLE products;");
//    }
//
//    @Nested
//    @DisplayName("상품 성공 케이스")
//    class success{
//
//        @Test
//        void 상품_등록() {
//            // given
//            ProductEntity entity = ProductStep.상품엔티티_기본값();
//
//            // when
//            ProductEntity saved = productRepository.save(entity);
//
//            // then
//            assertThat(saved).isNotNull();
//        }
//
//        @Test
//        void 상품_단건조회() {
//            // given
//            ProductEntity saved = productRepository.save(ProductStep.상품엔티티_기본값());
//
//            // when
//            ProductEntity found = productRepository.findById(saved.getId()).get();
//
//            // then
//            assertThat(found).isEqualTo(saved);
//        }
//
//        @Test
//        void 상품_전체조회() {
//            // given
//            productRepository.save(ProductStep.상품엔티티_기본값());
//            productRepository.save(ProductStep.상품엔티티_기본값());
//
//            // when
//            List<ProductEntity> found = productRepository.findAll();
//
//            // then
//            assertThat(found).hasSize(2);
//        }
//
//    }
//}
