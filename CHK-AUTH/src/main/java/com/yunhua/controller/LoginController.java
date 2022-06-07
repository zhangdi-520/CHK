package com.yunhua.controller;


import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.UserLoginRo;
import com.yunhua.feign.UserFeignService;
import com.yunhua.service.AuthLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private AuthLoginService authLoginService;

    @Autowired
    private UserFeignService userFeignService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody UserLoginRo user){
        return authLoginService.login(user);
    }


    @GetMapping("/user/sms")
    public ResponseResult sendSms(@RequestParam("mobile") String mobile){
        return userFeignService.sendSms(mobile);
    }
}
