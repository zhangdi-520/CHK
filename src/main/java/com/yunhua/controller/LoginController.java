package com.yunhua.controller;

import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.User;
import com.yunhua.service.LoginService;
import com.yunhua.sms.service.AliyunSmsSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @version V1.0
 * @program: CHK
 * @description: 用户登录接口
 * @author: Mr.Zhang
 * @create: 2022-05-08 15:08
 **/
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private AliyunSmsSenderService aliyunSmsSenderService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        return loginService.login(user);
    }

    @GetMapping("/user/loginout")
    public ResponseResult loginOut(){
        return loginService.loginOut();
    }


    @GetMapping("/user/sms")
    public ResponseResult sendSms(@RequestParam("mobile") String mobile){
        return aliyunSmsSenderService.sms(mobile);
    }
}
