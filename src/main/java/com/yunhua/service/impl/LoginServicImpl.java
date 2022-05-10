package com.yunhua.service.impl;

import com.yunhua.constant.RedisConstant;
import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.requestRo.UserLoginRo;
import com.yunhua.golbalexception.exception.BusinessException;
import com.yunhua.golbalexception.vo.ResultEnum;
import com.yunhua.security.domain.LoginUser;
import com.yunhua.security.util.JwtUtil;
import com.yunhua.service.LoginService;
import com.yunhua.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
    public ResponseResult login(UserLoginRo user) {
        //进行用户认证(直接用手机号加密生成密码)
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getMobile(), user.getMobile());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        //认证没通过给出提示
        if(Objects.isNull(authenticate)){
            throw new BusinessException(ResultEnum.LOFINERROR);
        }
        //认证通过生成JWT返回给前端
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        HashMap<String, String> map = new HashMap<>();
        map.put("token",jwt);
        //把完整信息存入redis(有效时长一小时)
        redisCache.setCacheObject(RedisConstant.LOGININFO +userId,loginUser,RedisConstant.LOFININFOEXPIRE, TimeUnit.SECONDS);
        return new ResponseResult(200,"登录成功", map);
    }

    @Override
    public ResponseResult loginOut() {
        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long id = loginUser.getUser().getId();
        //删除redis中缓存
        redisCache.deleteObject(RedisConstant.LOGININFO+id);
        return new ResponseResult(200,"注销成功");
    }

}
