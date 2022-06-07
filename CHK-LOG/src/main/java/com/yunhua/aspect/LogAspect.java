package com.yunhua.aspect;

import com.alibaba.fastjson.JSON;
import com.yunhua.annotation.WebLogAnnotation;
import com.yunhua.beans.WebLog;
import jdk.nashorn.internal.scripts.JO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Aspect // 切面，定义了通知和切点的关系
@Slf4j
public class LogAspect {
    @Pointcut("@annotation(com.yunhua.annotation.WebLogAnnotation)")
    public void pt(){}

    @Around("pt()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - startTime;
        //保存日志
        recordLog(point, startTime,time ,result);




        return result;
    }

    private void recordLog(ProceedingJoinPoint joinPoint,long startTime, long time , Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();


        WebLogAnnotation annotation = method.getAnnotation(WebLogAnnotation.class);
        log.info("=======================================");
        log.info("module:"+annotation.module());
        log.info("operator:"+annotation.operator());
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        log.info("methodNameAllPath:"+className+"."+methodName+"()");
        Object[] args = joinPoint.getArgs();
        String params = JSON.toJSONString(args);
        log.info("params:"+params);
        log.info("execute time:"+time);
        log.info("=======================================");


        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //记录请求信息
        WebLog webLog = new WebLog();


        webLog.setDescription("系统日志");
        webLog.setUsername("车管家用户");
        webLog.setIp(request.getRemoteUser());
        webLog.setMethod(request.getMethod());
        webLog.setParameter(getParameter(method, joinPoint.getArgs()).toString());
        webLog.setResult(result.toString());
        webLog.setSpendTime((int) (time));
        webLog.setStartTime(startTime);
        webLog.setUri(request.getRequestURI());
        webLog.setUrl(request.getRequestURL().toString());
        log.info("向消息队列发送操作日志消息{}", webLog.toString());
        kafkaTemplate.send(KafkaConstant.WEBLOG,webLog);
    }

    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable
    {
        long startTime = System.currentTimeMillis();
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //记录请求信息
        WebLog webLog = new WebLog();

        //前面是前置通知，后面是后置通知
        ResponseResult result = (ResponseResult)joinPoint.proceed();

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        long endTime = System.currentTimeMillis();
        webLog.setDescription("系统日志");
        webLog.setUsername("车管家用户");
        webLog.setIp(request.getRemoteUser());
        webLog.setMethod(request.getMethod());
        webLog.setParameter(getParameter(method, joinPoint.getArgs()).toString());
        webLog.setResult(result.toString());
        webLog.setSpendTime((int) (endTime - startTime));
        webLog.setStartTime(startTime);
        webLog.setUri(request.getRequestURI());
        webLog.setUrl(request.getRequestURL().toString());
        log.info("向消息队列发送操作日志消息{}", webLog.toString());

        return result;
    }

    /**
     * 根据方法和传入的参数获取请求参数
     */
    private Object getParameter(Method method, Object[] args)
    {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            //将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (!StringUtils.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.size() == 0) {
            return null;
        } else if (argList.size() == 1) {
            return argList.get(0);
        } else {
            return argList;
        }
    }
}
