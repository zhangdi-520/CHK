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

    /**
     * 单元测试这个方法无法生效原因，正常启动项目接口测试无问题
     * @Async 是在单元测试方法执行之后才开始执行的。 这时候单元测试框架单元测试已经完成了。
     * 所以将连接直接给关闭，直接结束程序的处理。
     * @param user
     */
    @Override
    @Async
    @Transactional
    public void insertUser(User user) {
        //插入DB
        userMapper.insertUser(user);
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
