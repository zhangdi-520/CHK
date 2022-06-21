package com.yunhua.aspect;

import com.yunhua.config.DBContextHolder;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

//定义AOP规则进行数据源的动态切换
@Component
@Aspect
public class DataSourceAop {

    @Pointcut("@annotation(com.yunhua.annotation.ReadOnly)" +
            "|| execution(* com.yunhua.mapper..*.find*(..))" +
            "|| execution(* com.yunhua.mapper..*.get*(..))")
    public void readPointcut(){

    }

    @Pointcut("!@annotation(com.yunhua.annotation.ReadOnly)" +
            "&& execution(* com.yunhua.mapper..*.insert*(..))" +
            "|| execution(* com.yunhua.mapper..*.update*(..))" +
            "|| execution(* com.yunhua.mapper..*.delete*(..))")
    public void writePointcut(){

    }

    @Before("readPointcut()")
    public void read(){
        DBContextHolder.slave();
    }

    @Before("writePointcut()")
    public void write(){
        DBContextHolder.master();
    }

    @After("readPointcut()")
    public void readAfter(){
        DBContextHolder.master();
    }
}
