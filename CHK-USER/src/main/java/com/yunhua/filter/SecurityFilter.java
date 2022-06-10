package com.yunhua.filter;

import com.yunhua.config.BasicConfig;
import com.yunhua.constant.RedisConstant;
import com.yunhua.execption.BusinessException;
import com.yunhua.execption.vo.ResultEnum;
import com.yunhua.utils.JwtUtil;
import com.yunhua.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * token校验
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    public RedisCache redisCache;

    @Autowired
    private BasicConfig basicConfig;

    @PostConstruct
    public void init(){
        //解析参数存入白名单队列
        redisCache.deleteObject(RedisConstant.WHITEURl);
        String openUrl = basicConfig.getOpenUrl();
        if (StringUtils.hasText(openUrl)){
            String[] split = openUrl.split(",");
            Set<String> whiteUrl = Arrays.stream(split).collect(Collectors.toSet());
            redisCache.setCacheSet(RedisConstant.WHITEURl,whiteUrl);
        }

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //校验是否在白名单里
        Set<Object> cacheSet = redisCache.getCacheSet(RedisConstant.WHITEURl);
        if (cacheSet.contains(request.getRequestURI())){
            filterChain.doFilter(request,response);
            return;
        }
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)){
            throw new BusinessException(ResultEnum.LOFINERROR);
        }
        //解析token
        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            throw new BusinessException(ResultEnum.TOKENNORULE);
        }
        //从redis中获取用户信息
        String redisKey = "login:"+userId;
        Object cacheObject = redisCache.getCacheObject(redisKey);
        if (Objects.isNull(cacheObject)){
            throw new BusinessException(ResultEnum.LOFINERROR);
        }
        //刷新用户过期时间,防止用户正在操作时登出
        redisCache.expire(redisKey +userId,RedisConstant.LOFININFOEXPIRE, TimeUnit.SECONDS);
        //校验通过，放行
        filterChain.doFilter(request,response);
    }
}
