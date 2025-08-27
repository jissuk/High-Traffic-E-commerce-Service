package kr.hhplus.be.server.user.listener;

import kr.hhplus.be.server.user.domain.model.PointHistory;
import kr.hhplus.be.server.user.domain.repository.PointHistoryRepository;
import kr.hhplus.be.server.user.event.PointChargeEvent;
import kr.hhplus.be.server.user.event.PointUseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PointHistoryListener {

    private final PointHistoryRepository pointHistoryRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void registerPointCharge(PointChargeEvent event) {
        PointHistory result = PointHistory.builder()
                .userId(event.userId())
                .point(event.point())
                .pointHistoryType(event.pointHistoryType())
                .createdAt(event.localDateTime())
                .build();

        pointHistoryRepository.save(result);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void registerPointUse(PointUseEvent event) {
        PointHistory result = PointHistory.builder()
                .userId(event.userId())
                .point(event.point())
                .pointHistoryType(event.pointHistoryType())
                .createdAt(event.localDateTime())
                .build();

        pointHistoryRepository.save(result);
    }
}
