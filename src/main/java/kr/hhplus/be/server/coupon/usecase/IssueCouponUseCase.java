package kr.hhplus.be.server.coupon.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.common.outbox.domain.repository.OutboxMessageRepository;
import kr.hhplus.be.server.common.outbox.domain.OutboxStatus;
import kr.hhplus.be.server.common.outbox.domain.model.OutboxMessage;
import kr.hhplus.be.server.coupon.exception.CouponOutOfStockException;
import kr.hhplus.be.server.coupon.exception.DuplicateCouponIssueException;
import kr.hhplus.be.server.coupon.usecase.command.UserCouponCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;

@UseCase
@RequiredArgsConstructor
public class IssueCouponUseCase {

    private final OutboxMessageRepository outBoxMessageRepository;
    private final ObjectMapper objectMapper;

    private final TransactionTemplate transactionTemplate;

    private final RedisTemplate<String, Long> redis;

    public static final String COUPON_ISSUE_PREFIX = "coupon:issue:";
    public static final String ISSUED_SUFFIX = ":issued";
    public static final String QUANTITY_SUFFIX = ":quantity";

    @DistributedLock
    public void execute(UserCouponCommand command) {
        /**
         * 1. 중복 쿠폰 발급 여부 (Redis 캐시)
         * 2. 쿠폰 수량 차감     (Redis 캐시)
         * 3. 쿠폰 발급
         * */
        validateDuplicateIssue(command);
        decrementQuantity(command);
        transactionTemplate.executeWithoutResult(status -> {
            try {
                saveOutboxMessage(command);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void validateDuplicateIssue(UserCouponCommand command) {
        String issuedKey = COUPON_ISSUE_PREFIX + command.userId() + ISSUED_SUFFIX;
        Boolean issuedResult = redis.opsForValue().getBit(issuedKey, command.userId());

        if(!issuedResult){
            redis.opsForValue().setBit(issuedKey, command.userId(), true);
        } else{
            throw new DuplicateCouponIssueException();
        }
    }

    private void decrementQuantity(UserCouponCommand command) {
        String quantityKey = COUPON_ISSUE_PREFIX + command.couponId() + QUANTITY_SUFFIX;
        Long quantity = redis.opsForValue().decrement(quantityKey);
        if(quantity == null || quantity < 0){
            throw new CouponOutOfStockException();
        }
    }

    private void saveOutboxMessage(UserCouponCommand command) throws JsonProcessingException {
        String jsonCommand = objectMapper.writeValueAsString(command);
        String issueCouponTopic = "coupon.issue.requested";
        OutboxMessage outBoxMessage = OutboxMessage.builder()
                                                    .topic(issueCouponTopic)
                                                    .payload(jsonCommand)
                                                    .status(OutboxStatus.PENDING)
                                                    .createdAt(LocalDateTime.now())
                                                    .build();
        outBoxMessageRepository.save(outBoxMessage);
    }
}
