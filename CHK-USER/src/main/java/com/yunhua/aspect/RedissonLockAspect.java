package com.yunhua.aspect;

import com.yunhua.annotation.RedissonReadLock;
import com.yunhua.annotation.RedissonWriteLock;
import com.yunhua.resolver.AnnotationResolver;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 监听读写锁信息
 */
@Aspect
@Component
@Slf4j
public class RedissonLockAspect {

    @Autowired
    private Redisson redisson;

    @Around(value = "@annotation(redissonReadLock)")
    public Object aroundRead(ProceedingJoinPoint joinpoint, RedissonReadLock redissonReadLock) throws Throwable {
        AnnotationResolver annotationResolver = AnnotationResolver.newInstance();
        Object resolver = annotationResolver.resolver(joinpoint, redissonReadLock.value());
        String value = redissonReadLock.value();
        String lockKey = value.split("-")[0]+"-"+resolver;
        RReadWriteLock readWriteLock = redisson.getReadWriteLock(lockKey);
        RLock rLock = readWriteLock.readLock();
        Object proceed = null;
        try {
            log.info("尝试获取读锁{}",lockKey);
            boolean b = rLock.tryLock(5, 10, TimeUnit.SECONDS);
            if (b) {
                proceed = joinpoint.proceed();
            }
        }finally {
            log.info("读锁{}解锁成功",lockKey);
            rLock.unlock();
        }

        return proceed;
    }


    @Around(value = "@annotation(redissonWriteLock)")
    public Object aroundWrite(ProceedingJoinPoint joinpoint, RedissonWriteLock redissonWriteLock) throws Throwable {
        AnnotationResolver annotationResolver = AnnotationResolver.newInstance();
        Object resolver = annotationResolver.resolver(joinpoint, redissonWriteLock.value());
        String value = redissonWriteLock.value();
        String lockKey = value.split("-")[0]+"-"+resolver;
        RReadWriteLock readWriteLock = redisson.getReadWriteLock(lockKey);
        RLock rLock = readWriteLock.writeLock();
        Object proceed = null;
        try {
            log.info("尝试获取写锁{}",lockKey);
            boolean b = rLock.tryLock(5, 10, TimeUnit.SECONDS);
            if (b) {
                proceed = joinpoint.proceed();
            }
        }finally {
            log.info("写锁{}解锁成功",lockKey);
            rLock.unlock();
        }

        return proceed;
    }
}
