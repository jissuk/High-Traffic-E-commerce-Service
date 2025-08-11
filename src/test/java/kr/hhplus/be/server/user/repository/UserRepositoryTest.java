//package kr.hhplus.be.server.user.repository;
//
//import kr.hhplus.be.server.user.domain.model.UserEntity;
//import kr.hhplus.be.server.user.domain.repository.UserRepository;
//import kr.hhplus.be.server.user.step.UserStep;
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
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Import(TestcontainersConfiguration.class)
//@DisplayName("유저 관련 테스트")
//public class UserRepositoryTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @BeforeEach
//    void setUp() {
//        clearTestData();
//    }
//
//    private void clearTestData() {
//        jdbcTemplate.execute("TRUNCATE TABLE users;");
//    }
//
//    @Nested
//    @DisplayName("성공 케이스")
//    class success {
//
//        @Test
//        void 유저_등록() {
//            // given
//            UserEntity user = UserStep.유저엔티티_기본값();
//
//            // when
//            UserEntity saved = userRepository.save(user);
//
//            // then
//            assertThat(saved).isNotNull();
//        }
//
//        @Test
//        void 유저_조회() {
//            // given
//            UserEntity saved = userRepository.save(UserStep.유저엔티티_기본값());
//
//            // when
//            UserEntity found = userRepository.findById(saved.getId()).get();
//
//            // then
//            assertThat(found).isEqualTo(saved);
//        }
//    }
//}
