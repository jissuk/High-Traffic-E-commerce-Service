package kr.hhplus.be.server.coupon.application.usecase;

import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.common.outbox.service.OutboxService;
import kr.hhplus.be.server.coupon.application.port.out.IssueCouponPort;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.coupon.infrastructure.kafka.event.IssueCouponEvent;
import kr.hhplus.be.server.coupon.application.command.UserCouponCommand;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class IssueCouponUseCase {

    private final OutboxService outboxService;
    private final IssueCouponPort port;

    @DistributedLock
    public void execute(UserCouponCommand command) {
        port.validateDuplicateIssue(command);
        port.decrementQuantity(command);
        publishIssueUserCouponEvent(command);
    }

    private void publishIssueUserCouponEvent(UserCouponCommand command) {
        UserCoupon userCoupon = UserCoupon.of(command);
        IssueCouponEvent event = IssueCouponEvent.of(userCoupon);
        outboxService.save(event);
    }
}