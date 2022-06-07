package com.yunhua.service.impl;

import com.alibaba.fastjson.JSON;
import com.yunhua.constant.RedisConstant;
import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.User;
import com.yunhua.domain.UserLoginRo;
import com.yunhua.execption.BusinessException;
import com.yunhua.execption.vo.ResultEnum;
import com.yunhua.feign.UserFeignService;
import com.yunhua.security.domain.LoginUser;
import com.yunhua.service.AuthLoginService;
import com.yunhua.util.RedisCache;
import com.yunhua.utils.CommonUtil;
import com.yunhua.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuthLoginServiceImpl implements AuthLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserFeignService userFeignService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult login(UserLoginRo user) {
        //权限校验之前先给用户进行注册
        String mobile = user.getMobile();
        String smsCode = user.getSmsCode();
        if (mobile== null || smsCode== null){
            log.info("短信登录验证必要参数为空");
            throw new BusinessException(ResultEnum.PARAMCHECHFAIL);
        }
        //手机号规范校验
        if (!CommonUtil.isMobile(mobile)){
            log.info("请填写正确的手机号");
            throw new BusinessException(ResultEnum.PARAMCHECHFAIL);
        }
        //验证缓存中短信验证码是否一致
        Object cacheObject = redisCache.getCacheObject("SMS:" + mobile);
        if (cacheObject != null){
            if (!smsCode.equals(String.valueOf(cacheObject).split("_")[0])) {
                log.info("短信验证码错误");
                throw new BusinessException(ResultEnum.SMSCODEERROR);
            }
        }
        ResponseResult userByMobile = userFeignService.findUserByMobile(mobile);
        Object data = userByMobile.getData();
        if (data == null){
            //注册
            User newUser = new User();
            newUser.setNickName("车管家_"+mobile.toString().substring(6));
            newUser.setPwd(passwordEncoder.encode(mobile.toString()));
            newUser.setMobile(mobile);
            newUser.setDelFlag(0);
            ResponseResult login = userFeignService.login(newUser);
            //注册成功进行鉴权
            if (login.getCode() != 200) {
                return new ResponseResult(-1, "登陆失败");
            }
        }else{
            User u = JSON.parseObject(JSON.toJSONString(data), User.class);
            if (u.getDelFlag() == 1){
                return new ResponseResult(-1,"用户未启用");
            }
        }
        //进行用户认证(直接用手机号加密生成密码)
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getMobile(), user.getMobile());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        //认证没通过给出提示
        if(Objects.isNull(authenticate)){
            throw new BusinessException(ResultEnum.LOFINERROR);
        }
        //认证通过生成JWT返回给前端
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        HashMap<String, String> map = new HashMap<>();
        map.put("token",jwt);
        //把完整信息存入redis(有效时长一小时)
        redisCache.setCacheObject(RedisConstant.LOGININFO +userId,loginUser,RedisConstant.LOFININFOEXPIRE, TimeUnit.SECONDS);
        return new ResponseResult(200,"登录成功", map);
    }

    @Override
    public ResponseResult loginOut() {
        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long id = loginUser.getUser().getId();
        //删除redis中缓存
        redisCache.deleteObject(RedisConstant.LOGININFO+id);
        return new ResponseResult(200,"注销成功");
    }
}
