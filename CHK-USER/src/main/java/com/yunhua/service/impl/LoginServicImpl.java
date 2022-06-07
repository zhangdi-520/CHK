package com.yunhua.service.impl;


import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.User;
import com.yunhua.service.LoginService;
import com.yunhua.service.UserService;
import com.yunhua.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @version V1.0
 * @program: CHK
 * @description: 用户登录实现类
 * @author: Mr.Zhang
 * @create: 2022-05-08 15:10
 **/
@Service
@Slf4j
public class LoginServicImpl implements LoginService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult login(User user) {
        userService.insertUser(user);
        log.info("=========================>手机号为{}的用户注册成功",user.getMobile());
        return new ResponseResult(200,"操作成功");
    }
}
