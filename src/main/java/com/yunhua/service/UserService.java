package com.yunhua.service;

import com.yunhua.domain.User;

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
}
