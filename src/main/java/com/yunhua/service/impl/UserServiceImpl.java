package com.yunhua.service.impl;

import com.yunhua.constant.RedisConstant;
import com.yunhua.domain.User;
import com.yunhua.mapper.UserMapper;
import com.yunhua.service.UserService;
import com.yunhua.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @version V1.0
 * @program: CHK
 * @description: 用户操作实现类
 * @author: Mr.Zhang
 * @create: 2022-05-10 13:48
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    @Async
    public void insertUser(User user) {
        //插入DB
        userMapper.insertUser(user);
        System.out.println("===========================:dajidbabdhua:"+user.toString());
        //用户数据存入缓存
        redisCache.setCacheObject(RedisConstant.USERINFO + user.getMobile(), user, RedisConstant.USERINFOEXOIRE, TimeUnit.DAYS);
    }

    @Override
    public User findUserByMobile(String mobile) {
        User user = redisCache.getCacheObject(RedisConstant.USERINFO + mobile);
        if (Objects.isNull(user)) {
            user = userMapper.findUserByMobile(mobile);
        }
        //DB中有数据往缓存中写一份
        if (!Objects.isNull(user)) {
            redisCache.setCacheObject(RedisConstant.USERINFO + user.getMobile(), user, RedisConstant.USERINFOEXOIRE, TimeUnit.DAYS);
        }
        return user;
    }


}
