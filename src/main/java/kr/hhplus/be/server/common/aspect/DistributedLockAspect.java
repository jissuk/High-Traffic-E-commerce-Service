package kr.hhplus.be.server.common.aspect;

import kr.hhplus.be.server.common.annotation.DistributedLock;
import kr.hhplus.be.server.common.provider.LockKeyProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DistributedLockAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(kr.hhplus.be.server.common.annotation.DistributedLock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key = StringUtils.EMPTY;

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if(arg instanceof LockKeyProvider lockKeyProvider){
                key = lockKeyProvider.lockKey();
            }
        }

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