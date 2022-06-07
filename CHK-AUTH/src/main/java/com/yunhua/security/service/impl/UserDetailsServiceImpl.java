package com.yunhua.security.service.impl;

import com.alibaba.fastjson.JSON;
import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.User;
import com.yunhua.feign.UserFeignService;
import com.yunhua.security.domain.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @version V1.0
 * @program: CHK
 * @description: springSecurity自定义校验逻辑
 * @author: Mr.Zhang
 * @create: 2022-05-07 17:44
 **/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserFeignService userFeignService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        ResponseResult responseResult = userFeignService.selectOne(username);
        User user = JSON.parseObject(JSON.toJSONString(responseResult.getData()),User.class);
        System.out.println("=============:"+user);
        //用户为空抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或者密码错误");
        }
        //TODO查询对应的权限信息
        ResponseResult menu = userFeignService.selectPermsByUserId(user.getId());
        List<String> list = JSON.parseObject(JSON.toJSONString(menu.getData()),List.class);
        return new LoginUser(user,list);
    }
}
