package kr.hhplus.be.server.common.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Order(2)
@Aspect
@Component
@RequiredArgsConstructor
public class  TransactionalAspect {

    private final PlatformTransactionManager transactionManager;

    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    public Object aroundTransactional(ProceedingJoinPoint joinPoint) throws Throwable {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName(joinPoint.getSignature().toShortString());
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            Object result = joinPoint.proceed();
            transactionManager.commit(status);
            return result;
        } catch (Throwable ex) {
            transactionManager.rollback(status);
            throw ex;
        }
    }
}