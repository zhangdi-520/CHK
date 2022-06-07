package com.yunhua.service;

import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.UserLoginRo;


public interface AuthLoginService {

     ResponseResult login(UserLoginRo user);

     ResponseResult loginOut();
}
