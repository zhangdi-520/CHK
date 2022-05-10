package com.yunhua.service;

import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.requestRo.UserLoginRo;

/**
 * @version V1.0
 * @program: CHK
 * @description: 登录接口类
 * @author: Mr.Zhang
 * @create: 2022-05-08 15:10
 **/
public interface LoginService {

    ResponseResult login(UserLoginRo user);

    ResponseResult loginOut();
}
