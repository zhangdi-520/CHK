package com.yunhua.service.impl;

import com.yunhua.annotation.RedissonReadLock;
import com.yunhua.constant.LockConstant;
import com.yunhua.constant.RedisConstant;
import com.yunhua.domain.User;
import com.yunhua.mapper.UserMapper;
import com.yunhua.service.UserService;
import com.yunhua.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 缓存采用写失效模式，insert时不用处理缓存
     * @param user
     */
    @Override
    @Async
    @Transactional
    public void insertUser(User user) {
        //插入DB
        userMapper.insertUser(user);
    }

    /**
     * 使用springCache启用一起的缓存流程 ，加sync开启单机锁防止大并发下缓存击穿问题
     * @param mobile
     * @return
     */
    @RedissonReadLock(value = LockConstant.USER+"#mobile")
    @Cacheable(value = {RedisConstant.USERINFO},key = "#root.args[0]",sync = true)
    @Override
    public User findUserByMobile(String mobile) {
        User  user = userMapper.findUserByMobile(mobile);
        return user;
    }


}
