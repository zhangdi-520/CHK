package com.yunhua.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunhua.domain.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @version V1.0
 * @program: CHK
 * @description: 自定义线程池拒绝策略
 * @author: Mr.Zhang
 * @create: 2022-05-11 11:14
 **/
@Component
@Slf4j
public class ThreadPoolRejectHandler implements RejectedExecutionHandler {


    /**
     * 先获取方法信息然后后续根据业务觉得用中间件或者什么方法记录并重新执行
     * @param r
     * @param executor
     */

    @Override
    @SneakyThrows
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

        Field field=r.getClass().getDeclaredField("callable");
        field.setAccessible(true);
        Callable callable=(Callable)field.get(r);
        Field invocationField=callable.getClass().getDeclaredField("arg$2");
        invocationField.setAccessible(true);
        MethodInvocation invocation=(MethodInvocation)invocationField.get(callable);
        Object[] args=invocation.getArguments();
        User object=(User) args[0];
        log.info("拒绝策略拒绝任务:{}",new ObjectMapper().writeValueAsString(object));

        //可以用LinkedBlokingQueue的put方法阻塞入队
        //executor.getQueue().put(r);
    }
}
