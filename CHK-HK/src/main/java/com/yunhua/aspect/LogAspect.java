package com.yunhua.aspect;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunhua.annotation.WebLogAnnotation;
import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.WebLog;
import com.yunhua.constant.KafkaConstant;
import com.yunhua.utils.HttpContextUtils;
import com.yunhua.utils.IpUtils;
import com.yunhua.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Component
@Aspect // 切面，定义了通知和切点的关系
@Slf4j
public class LogAspect {

    @Autowired
    private KafkaTemplate<Object,Object> kafkaTemplate;


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


    /**
     * 将Log日志发送到kafka中，这里默认是携带了token，如果是未登录的
     * @param joinPoint
     * @param startTime
     * @param time
     * @param result
     */

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
//        HttpServletRequest request = attributes.getRequest();
        //记录请求信息
        WebLog webLog = new WebLog();

        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        String token = request.getHeader("token");

        //获取token
        if (!StringUtils.hasText(token)){
            //如果token为空，那么就不用将日志存到ES数据库中
            return;
        }
        //解析token
        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            //如果token解析异常，那么就不用将日志存到ES数据库中
            return;
        }


        ObjectMapper objectMapper = new ObjectMapper();
        ResponseResult responseResult = objectMapper.convertValue(result, ResponseResult.class);
        String ipAddr = IpUtils.getIpAddr(request);
        log.info("ipAddr:"+ipAddr);

        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");

        webLog.setId(uuidStr);
        webLog.setDescription("系统日志-"+annotation.module()+"模块");
        webLog.setUserId(Long.parseLong(userId));
        webLog.setIp(ipAddr);
        webLog.setCode(responseResult.getCode());
        webLog.setMethod(request.getMethod());
        webLog.setParameter(getParameter(method, joinPoint.getArgs()).toString());
        webLog.setResult(result.toString());
        webLog.setSpendTime((int) (time));
        webLog.setStartTime(startTime);
        webLog.setUri(request.getRequestURI());
        webLog.setUrl(request.getRequestURL().toString());
        log.info("向消息队列发送操作日志消息{}", webLog.toString());
        kafkaTemplate.send(KafkaConstant.WEBLOG,JSON.toJSONString(webLog));
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
            PathVariable pathVariable = parameters[i].getAnnotation(PathVariable.class);
            if (pathVariable != null){
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (!StringUtils.isEmpty(pathVariable.value())) {
                    key = pathVariable.value();
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
