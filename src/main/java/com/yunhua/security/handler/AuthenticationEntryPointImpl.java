package com.yunhua.security.handler;

import com.alibaba.fastjson.JSON;
import com.yunhua.domain.ResponseResult;
import com.yunhua.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Object errorMsg = request.getAttribute("errorMsg");
        if (errorMsg == null) {
            errorMsg = "认证失败请重新登录";
        }
        ResponseResult result = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), errorMsg);
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json);
    }
}