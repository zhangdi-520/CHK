//package com.yunhua.aspect;
//
//import com.yunhua.domain.ResponseResult;
//import com.yunhua.kafka.constant.KafkaConstant;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.annotation.Order;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//import java.lang.reflect.Parameter;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @version V1.0
// * @program: CHK
// * @description: 日志切面类
// * @author: Mr.Zhang
// * @create: 2022-05-11 15:18
// **/
//@Aspect
//@Component
//@Order(1)
//@Slf4j
//public class WebLogAspect {
//
//    @Autowired
//    private KafkaTemplate<Object,Object> kafkaTemplate;
//
//    //定义切点表达式,指定通知功能被应用的范围
//    @Pointcut("execution(public * com.yunhua.controller.*.*(..))")
//    public void webLog() {
//    }
//
//    @Before("webLog()")
//    public void doBefore(JoinPoint joinPoint) throws Throwable {
//    }
//
//    /**value切入点位置
//     * returning 自定义的变量，标识目标方法的返回值,自定义变量名必须和通知方法的形参一样
//     * 特点：在目标方法之后执行的,能够获取到目标方法的返回值，可以根据这个返回值做不同的处理
//     */
//    @AfterReturning(value = "webLog()", returning = "ret")
//    public void doAfterReturning(Object ret) throws Throwable {
//    }
//
//    //通知包裹了目标方法，在目标方法调用之前和之后执行自定义的行为
//    //ProceedingJoinPoint切入点可以获取切入点方法上的名字、参数、注解和对象
////    @Around("webLog()")
//    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable
//    {
//        long startTime = System.currentTimeMillis();
//        //获取当前请求对象
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        //记录请求信息
//        WebLog webLog = new WebLog();
//
//        //前面是前置通知，后面是后置通知
//        ResponseResult result = (ResponseResult)joinPoint.proceed();
//
//        Signature signature = joinPoint.getSignature();
//        MethodSignature methodSignature = (MethodSignature) signature;
//        Method method = methodSignature.getMethod();
//        long endTime = System.currentTimeMillis();
//        webLog.setDescription("系统日志");
//        webLog.setUsername("车管家用户");
//        webLog.setIp(request.getRemoteUser());
//        webLog.setMethod(request.getMethod());
//        webLog.setParameter(getParameter(method, joinPoint.getArgs()).toString());
//        webLog.setResult(result.toString());
//        webLog.setSpendTime((int) (endTime - startTime));
//        webLog.setStartTime(startTime);
//        webLog.setUri(request.getRequestURI());
//        webLog.setUrl(request.getRequestURL().toString());
//        log.info("向消息队列发送操作日志消息{}", webLog.toString());
//        kafkaTemplate.send(KafkaConstant.WEBLOG,webLog);
//        return result;
//    }
//
//    /**
//     * 根据方法和传入的参数获取请求参数
//     */
//    private Object getParameter(Method method, Object[] args)
//    {
//        List<Object> argList = new ArrayList<>();
//        Parameter[] parameters = method.getParameters();
//        for (int i = 0; i < parameters.length; i++) {
//            //将RequestBody注解修饰的参数作为请求参数
//            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
//            if (requestBody != null) {
//                argList.add(args[i]);
//            }
//            //将RequestParam注解修饰的参数作为请求参数
//            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
//            if (requestParam != null) {
//                Map<String, Object> map = new HashMap<>();
//                String key = parameters[i].getName();
//                if (!StringUtils.isEmpty(requestParam.value())) {
//                    key = requestParam.value();
//                }
//                map.put(key, args[i]);
//                argList.add(map);
//            }
//        }
//        if (argList.size() == 0) {
//            return null;
//        } else if (argList.size() == 1) {
//            return argList.get(0);
//        } else {
//            return argList;
//        }
//    }
//
//}
