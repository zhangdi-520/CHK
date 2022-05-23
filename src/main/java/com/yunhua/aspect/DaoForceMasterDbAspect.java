package com.yunhua.aspect;

import org.apache.shardingsphere.api.hint.HintManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 特定业务场景下查询需要强制走主库
 */
@Aspect
//@Component
public class DaoForceMasterDbAspect {

    /**
     * 这边监听的路径根据业务场景定
     * @param joinpoint
     * @return
     * @throws Throwable
     */
//    @Around("execution(* jiagoubaiduren.dao..*.*(..))")
    public Object around(ProceedingJoinPoint joinpoint) throws Throwable {
        HintManager.getInstance().setMasterRouteOnly();
        return joinpoint.proceed();
    }
}
