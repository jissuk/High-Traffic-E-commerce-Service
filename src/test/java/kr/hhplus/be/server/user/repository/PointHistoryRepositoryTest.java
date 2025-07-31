package kr.hhplus.be.server.user.repository;

import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.user.domain.model.PointHistoryEntity;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import kr.hhplus.be.server.user.domain.repository.PointHistoryRepository;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.step.UserStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(TestcontainersConfiguration.class)
@DisplayName("유저 포인트 내역 관련 테스트")
public class PointHistoryRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        clearTestData();
    }

    private void clearTestData() {
        jdbcTemplate.execute("TRUNCATE TABLE tbl_user;");
        jdbcTemplate.execute("TRUNCATE TABLE tbl_point_history;");
    }

    @Nested
    @DisplayName("성공 케이스")
    class success{

        @Test
        void 포인트내역_등록() {
            // given
            UserEntity user = userRepository.save(UserStep.유저엔티티_기본값());
            PointHistoryEntity entity = UserStep.포인트내역엔티티_기본값(user);

            // when
            PointHistoryEntity saved = pointHistoryRepository.save(entity);

            // then
            assertThat(saved).isNotNull();
        }
    }


}
