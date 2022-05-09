package com.yunhua.service.impl;

import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.User;
import com.yunhua.security.domain.LoginUser;
import com.yunhua.security.util.JwtUtil;
import com.yunhua.service.LoginService;
import com.yunhua.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

/**
 * @version V1.0
 * @program: CHK
 * @description: 用户登录实现类
 * @author: Mr.Zhang
 * @create: 2022-05-08 15:10
 **/
@Service
public class LoginServicImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        //进行用户认证
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        //认证没通过给出提示
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("登录失败");
        }
        //认证通过生成JWT返回给前端
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        HashMap<String, String> map = new HashMap<>();
        map.put("token",jwt);
        //把完整信息存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);
        return new ResponseResult(200,"登录成功", map);
    }

    @Override
    public ResponseResult loginOut() {
        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long id = loginUser.getUser().getId();
        //删除redis中缓存
        redisCache.deleteObject("login:"+id);
        return new ResponseResult(200,"注销成功");
    }

}
