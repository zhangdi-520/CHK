package com.yunhua.security.handler;

import com.alibaba.fastjson.JSON;
import com.yunhua.domain.ResponseResult;
import com.yunhua.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Object errorMsg = request.getAttribute("errorMsg");
        if (errorMsg == null){
            errorMsg = "权限不足";
        }
        ResponseResult result = new ResponseResult(HttpStatus.FORBIDDEN.value(),errorMsg );
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json);
    }
}