package com.yunhua.security.filter;

import com.alibaba.fastjson.JSON;
import com.yunhua.config.BodyReaderHttpServletRequestWrapper;
import com.yunhua.domain.User;
import com.yunhua.mapper.UserMapper;
import com.yunhua.service.UserService;
import com.yunhua.utils.CommonUtil;
import com.yunhua.utils.HttpHelper;
import com.yunhua.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @version V1.0
 * @program: CHK
 * @description: 自定义验证码校验filter
 * @author: Mr.Zhang
 * @create: 2022-05-10 10:15
 **/
@Component
@Slf4j
public class SmsCodeValidateFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //只针对验证码登录接口
        if (request.getRequestURI().equals("/chk/user/login")
        && request.getMethod().equalsIgnoreCase("post")){
            //复制一份流到新流中，HttpHelper.getBodyMap会将原本流的数据取出来
            BodyReaderHttpServletRequestWrapper requestWrapper = null;
            if (request instanceof HttpServletRequest) {
                requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request);
            }
            byte[] body = requestWrapper.getBody();
            String bodyString = new String(body);
            Map bodyMap = JSON.parseObject(bodyString, Map.class);
            Object mobile = bodyMap.get("mobile");
            Object smsCode = bodyMap.get("smsCode");
            if (mobile== null || smsCode== null){
                request.setAttribute("errorMsg","短信登录验证必要参数为空");
                throw new RuntimeException("短信登录验证必要参数为空");
            }
            //手机号规范校验
            if (!CommonUtil.isMobile(mobile.toString())){
                request.setAttribute("errorMsg","请填写正确的手机号");
                throw new RuntimeException("请填写正确的手机号");
            }
            //验证缓存中短信验证码是否一致
            Object cacheObject = redisCache.getCacheObject("SMS:" + mobile);
            if (!smsCode.equals(cacheObject)){
                request.setAttribute("errorMsg","短信验证码错误");
                throw new RuntimeException("短信验证码错误");
            }
            //检验通过查询用户是否注册，未注册情况下注册
            User userByMobile = userService.findUserByMobile(mobile.toString());
            if (userByMobile == null){
                //注册
                User user = new User();
                user.setNickName("车管家_"+mobile.toString().substring(6));
                user.setPwd(passwordEncoder.encode(mobile.toString()));
                user.setMobile(mobile.toString());
                user.setDelFlag(0);
                userService.insertUser(user);
                log.info("=========================>手机号为{}的用户注册成功",mobile);
                //放行
                filterChain.doFilter(requestWrapper,response);
                return;
            }
            if ("1".equals(userByMobile.getDelFlag())){
                request.setAttribute("errorMsg","用户未启用");
                throw new RuntimeException("用户未启用");
            }
        }
        //不属于验证码登录接口直接放行
        filterChain.doFilter(request,response);
    }



}
