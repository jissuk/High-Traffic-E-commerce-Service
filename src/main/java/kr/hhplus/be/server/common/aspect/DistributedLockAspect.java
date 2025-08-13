package kr.hhplus.be.server.common.aspect;

import kr.hhplus.be.server.common.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Order(1)
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final RedissonClient  redissonClient;

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String key = distributedLock.key();

        RLock lock = redissonClient.getLock(key);
        boolean acquired = lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), TimeUnit.SECONDS);

        if (!acquired) {
            throw new RuntimeException("락 획득 실패");
        }

        try {
            return joinPoint.proceed();
        } finally {
            lock.unlock();
        }
    }
}