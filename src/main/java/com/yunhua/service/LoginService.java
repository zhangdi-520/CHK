package com.yunhua.service;

import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.User;

/**
 * @version V1.0
 * @program: CHK
 * @description: TODO
 * @author: Mr.Zhang
 * @create: 2022-05-08 15:10
 **/
public interface LoginService {

    ResponseResult login(User user);

    ResponseResult loginOut();
}
