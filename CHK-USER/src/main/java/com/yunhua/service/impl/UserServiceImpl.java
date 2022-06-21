package com.yunhua.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yunhua.annotation.MyRedissonReadLock;
import com.yunhua.annotation.ReadOnly;
import com.yunhua.constant.LockConstant;
import com.yunhua.constant.RedisConstant;
import com.yunhua.dao.UserDao;
import com.yunhua.domain.User;
import com.yunhua.mapper.MenuMapper;
import com.yunhua.mapper.UserMapper;
import com.yunhua.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @version V1.0
 * @program: CHK
 * @description: 用户操作实现类
 * @author: Mr.Zhang
 * @create: 2022-05-10 13:48
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MenuMapper menuMapper;


    /**
     * 单元测试这个方法无法生效原因，正常启动项目接口测试无问题
     * @Async 是在单元测试方法执行之后才开始执行的。 这时候单元测试框架单元测试已经完成了。
     * 所以将连接直接给关闭，直接结束程序的处理。
     * 缓存采用写失效模式，insert时不用处理缓存
     * @param user
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {RedisConstant.USERINFO},key = "#user.mobile" )
    public void insertUser(User user) {
        System.out.println("执行插入操作");
        //插入DB
        userMapper.insertUser(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {RedisConstant.USERINFO},key = "#user.mobile" )
    public int addUser(User user) {
        int insertStatus = userDao.addUser(user);

        return insertStatus;
    }

    /**
     * 使用springCache启用一起的缓存流程 ，加sync开启单机锁防止大并发下缓存击穿问题
     * @param mobile
     * @return
     */
    @MyRedissonReadLock(value = LockConstant.USER+"#mobile")
    @Cacheable(value = {RedisConstant.USERINFO},key = "#root.args[0]",sync = true)
    @Override
    @ReadOnly
    public User findUserByMobile(String mobile) {
        User  user = userMapper.findUserByMobile(mobile);
        log.info("通过手机号{}查寻到的用户{}",mobile,user);
        return user;
    }


    @Override
    @ReadOnly
    public User selectOne(String mobile) {
        return userMapper.findUserByMobile(mobile);
    }

    @Override
    @ReadOnly
    public List<String> selectPermsByUserId(Long userId) {
        return menuMapper.selectPermsByUserId(userId);
    }


    /**
     * 从用户中心查询用户
     * @param user
     * @return
     */

    @Override
    @ReadOnly
    public User selectUserByMobileInDataCenter(User user) {
        User userCenter = userDao.selectUserByMobileInDataCenter(user);
        return userCenter;
    }


}
