package com.yunhua.filter;

import com.yunhua.execption.BusinessException;
import com.yunhua.execption.vo.ResultEnum;
import com.yunhua.utils.JwtUtil;
import com.yunhua.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * token校验
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    public RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
//        String token = request.getHeader("token");
//        if (!StringUtils.hasText(token)){
//            throw new BusinessException(ResultEnum.LOFINERROR);
//        }
//        //解析token
//        String userId;
//        try {
//            Claims claims = JwtUtil.parseJWT(token);
//            userId = claims.getSubject();
//        } catch (Exception e) {
//            throw new BusinessException(ResultEnum.TOKENNORULE);
//        }
//        //从redis中获取用户信息
//        String redisKey = "login:"+userId;
//        Object cacheObject = redisCache.getCacheObject(redisKey);
//        if (Objects.isNull(cacheObject)){
//            throw new BusinessException(ResultEnum.LOFINERROR);
//        }
        //校验通过，放行
        filterChain.doFilter(request,response);
    }
}
