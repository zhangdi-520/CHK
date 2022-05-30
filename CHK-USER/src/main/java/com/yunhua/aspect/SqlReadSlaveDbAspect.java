package com.yunhua.aspect;


import com.yunhua.annotation.SqlReadSlave;
import org.apache.shardingsphere.api.hint.HintManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 监听到注解读取从库
 */
@Aspect
@Component
public class SqlReadSlaveDbAspect {

    @Around(value = "@annotation(sqlReadSlave)")
    public Object around(ProceedingJoinPoint joinpoint, SqlReadSlave sqlReadSlave) throws Throwable {
        if (HintManager.isMasterRouteOnly()) {
            HintManager.clear();
        }

        return joinpoint.proceed();
    }
}
