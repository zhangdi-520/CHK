package com.yunhua.controller;

import com.yunhua.annotation.WebLogAnnotation;
import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.User;
import com.yunhua.execption.vo.ResultEnum;
import com.yunhua.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "用户信息接口",tags = "用户信息")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/get/{mobile}")
    @ApiOperation("根据用户的电话查询用户信息")
    public ResponseResult getUserByMobileInDataCenter(@PathVariable("mobile") String mobile){
        User user = userService.findUserByMobile(mobile);
        if (user == null){
            return new ResponseResult(ResultEnum.NOTFINDINDATABASE.getCode(), ResultEnum.NOTFINDINDATABASE.getMsg());
        }
        return new ResponseResult<>(200, user);
    }
}
