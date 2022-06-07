package com.yunhua.feign;

import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 声明式远程调用其他服务
 */
@FeignClient(name = "chk-user")
public interface UserFeignService {

    @PostMapping("/chk-user/user/login")
    public ResponseResult login(@RequestBody User user);

    @GetMapping("/chk-user/findUserByMobile")
    public ResponseResult findUserByMobile(@RequestParam("mobile") String mobile);

    @GetMapping("/chk-user/user/sms")
    public ResponseResult sendSms(@RequestParam("mobile") String mobile);


    @GetMapping("/chk-user/selectOne")
    public ResponseResult selectOne(@RequestParam("username") String username);

    @GetMapping("/chk-user/selectPermsByUserId")
    public ResponseResult selectPermsByUserId(@RequestParam("selectPermsByUserId") Long userId);
}
