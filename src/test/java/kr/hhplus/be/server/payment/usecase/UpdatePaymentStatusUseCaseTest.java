package kr.hhplus.be.server.payment.usecase;

import kr.hhplus.be.server.payment.domain.Repository.PaymentHistoryRepository;
import kr.hhplus.be.server.payment.domain.Repository.PaymentRepository;
import kr.hhplus.be.server.payment.domain.mapper.PaymentHistoryMapper;
import kr.hhplus.be.server.payment.domain.mapper.PaymentMapper;
import kr.hhplus.be.server.payment.domain.model.PaymentEntity;
import kr.hhplus.be.server.payment.domain.model.PaymentHistoryEntity;
import kr.hhplus.be.server.payment.exception.PaymentNotFoundException;
import kr.hhplus.be.server.payment.step.PaymentStep;
import kr.hhplus.be.server.payment.usecase.command.PaymentCommand;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
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
    @DisplayName("결제 상태 수정 성공 케이스")
    class success{

        @Test
        @DisplayName("결제가 존재할 경우 결제 상태를 수정한다.")
        void 결제상태수정(){
            // given
            PaymentCommand command = PaymentStep.결제커맨드_기본값();
            when(paymentRepository.findById(command.paymentId())).thenReturn(PaymentStep.결제엔티티_기본값());

            // when
            updatePaymentStatusUseCase.execute(command);

            // then
            verify(paymentRepository).update(any(PaymentEntity.class));
            verify(paymentHistoryRepository).save(any(PaymentHistoryEntity.class));
        }
    }

    @Nested
    @DisplayName("결제 상태 수정 실패 케이스")
    class fail{

        @Test
        @DisplayName("존재하지 않는 주문일 경우 PaymentNotFoundException이 발생한다.")
        void 결제상태수정_존재하지않는_주문일_경우(){
            // given
            PaymentCommand command = PaymentStep.결제커맨드_기본값();
            when(paymentRepository.findById(command.paymentId())).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> updatePaymentStatusUseCase.execute(command))
                    .isInstanceOf(PaymentNotFoundException.class);
        }
    }

}
