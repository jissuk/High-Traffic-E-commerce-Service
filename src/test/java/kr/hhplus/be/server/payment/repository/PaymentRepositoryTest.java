package kr.hhplus.be.server.payment.repository;


import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import kr.hhplus.be.server.payment.step.PaymentStep;
import kr.hhplus.be.server.user.domain.model.UserEntity;
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
@DisplayName("결제 관련 테스트")
public class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        clearTestData();
    }
    private void clearTestData() {
        jdbcTemplate.execute("TRUNCATE TABLE payments;");
    }
    @Nested
    @DisplayName("결제 성공 케이스")
    class success{

        @Test
        void 결제_등록 () {
            // given
            UserEntity user = UserStep.유저엔티티_기본값();
            PaymentEntity entity = PaymentStep.결제엔티티_기본값(user);

            // when
            PaymentEntity saved = paymentRepository.save(entity);

            // then
            assertThat(saved).isNotNull();
        }

        @Test
        void 결제_조회 () {
            // given
            UserEntity user = UserStep.유저엔티티_기본값();
            PaymentEntity saved = paymentRepository.save(PaymentStep.결제엔티티_기본값(user));

            // when
            PaymentEntity found = paymentRepository.findById(saved.getId()).get();

            // then
            assertThat(found).isEqualTo(saved);
        }
    }
}
