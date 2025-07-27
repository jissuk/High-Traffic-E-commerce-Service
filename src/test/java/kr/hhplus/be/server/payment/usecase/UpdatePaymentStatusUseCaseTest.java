package kr.hhplus.be.server.payment.usecase;

import kr.hhplus.be.server.payment.domain.Repository.PaymentHistoryRepository;
import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.domain.mapper.PaymentHistoryMapper;
import kr.hhplus.be.server.payment.domain.mapper.PaymentMapper;
import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import kr.hhplus.be.server.payment.domain.model.PaymentHistoryEntity;
import kr.hhplus.be.server.payment.exception.PaymentNotFoundException;
import kr.hhplus.be.server.payment.step.PaymentStep;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequestDTO;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("결제 상태 수정 테스트")
@ExtendWith(MockitoExtension.class)
public class UpdatePaymentStatusUseCaseTest {

    private UpdatePaymentStatusUseCase updatePaymentStatusUseCase;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentHistoryRepository paymentHistoryRepository;

    @BeforeEach
    void setUp() {
        PaymentMapper paymentMapper = Mappers.getMapper(PaymentMapper.class);
        PaymentHistoryMapper  paymentHistoryMapper = Mappers.getMapper(PaymentHistoryMapper.class);
        updatePaymentStatusUseCase = new UpdatePaymentStatusUseCase(
                paymentRepository,
                paymentHistoryRepository,
                paymentMapper,
                paymentHistoryMapper
        );
    }

    @Nested
    @DisplayName("성공 케이스")
    class success{
        @Test
        void 결제상태수정(){
            // given
            PaymentRequestDTO request = PaymentStep.기본결제요청생성();
            when(paymentRepository.findById(request.getPaymentId())).thenReturn(PaymentStep.기본결제엔티티생성());

            // when
            updatePaymentStatusUseCase.execute(request);

            // then
            verify(paymentRepository).update(any(PaymentEntity.class));
            verify(paymentHistoryRepository).save(any(PaymentHistoryEntity.class));
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class fail{
        @Test
        void 결제상태수정_존재하지않는_주문일_경우(){
            // given
            PaymentRequestDTO request = PaymentStep.기본결제요청생성();
            when(paymentRepository.findById(request.getPaymentId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> updatePaymentStatusUseCase.execute(request))
                    .isInstanceOf(PaymentNotFoundException.class);
        }
    }

}
