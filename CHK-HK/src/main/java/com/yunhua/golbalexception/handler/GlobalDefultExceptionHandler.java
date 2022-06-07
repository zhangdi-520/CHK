package com.yunhua.golbalexception.handler;


import com.yunhua.entity.vo.ResponseResult;
import com.yunhua.golbalexception.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @version V1.0
 * @program: CHK
 * @description: 全局异常处理器
 * @author: Mr.Zhang
 * @create: 2022-05-10 16:13
 **/
@ControllerAdvice
@Slf4j
public class GlobalDefultExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult defultExceptionHandler(HttpServletRequest request, Exception e){
        if(e instanceof BusinessException){
            log.error("业务异常:"+e.getMessage());
            BusinessException businessException = (BusinessException)e;
            return new ResponseResult(businessException.getCode(),businessException.getMessage());
        }
        e.printStackTrace();
        return new ResponseResult(-1,e);
    }
}
