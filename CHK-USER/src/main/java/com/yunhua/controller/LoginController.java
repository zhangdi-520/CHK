package com.yunhua.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.User;
import com.yunhua.service.LoginService;
import com.yunhua.service.UserService;
import com.yunhua.sms.service.AliyunSmsSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Autowired
    private UserService userService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        return loginService.login(user);
    }


    @RequestMapping("/user/sms")
    public ResponseResult sendSms(@RequestParam("mobile") String mobile){
        return aliyunSmsSenderService.sms(mobile);
    }

    @GetMapping("/findUserByMobile")
    public ResponseResult findUserByMobile(@RequestParam("mobile") String mobile){
        User userByMobile = userService.findUserByMobile(mobile);
        return new ResponseResult(200,userByMobile);
    }

    @GetMapping("/selectOne")
    public ResponseResult selectOne(@RequestParam("username") String username){
        User user = userService.findUserByMobile(username);
        return new ResponseResult(200,user);
    }

    @GetMapping("/selectPermsByUserId")
    public ResponseResult selectPermsByUserId(@RequestParam("selectPermsByUserId") Long userId){
        List<String> strings = userService.selectPermsByUserId(userId);
        return new ResponseResult(200,strings);
    }


}
