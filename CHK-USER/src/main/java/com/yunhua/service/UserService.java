package com.yunhua.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.User;

import java.util.List;

/**
 * @version V1.0
 * @program: CHK
 * @description: 用户服务接口类
 * @author: Mr.Zhang
 * @create: 2022-05-10 13:48
 **/
public interface UserService {

    void insertUser(User user);

    User findUserByMobile(String mobile);

    User selectOne(String mobile);

    List<String> selectPermsByUserId(Long userId);
}
