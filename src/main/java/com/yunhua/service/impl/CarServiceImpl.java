package com.yunhua.service.impl;

import com.yunhua.domain.ChkUserCarInfo;
import com.yunhua.domain.ResponseResult;
import com.yunhua.mapper.CarMapper;
import com.yunhua.security.util.JwtUtil;
import com.yunhua.service.CarService;
import com.yunhua.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @version V1.0
 * @program: CHK
 * @description: 车辆服务实现类
 * @author: Mr.Zhang
 * @create: 2022-05-12 09:40
 **/
@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private CarMapper carMapper;

    @Override
    public ResponseResult addUserCar(String token, ChkUserCarInfo chkUserCarInfo) throws Exception {
        Claims claims = JwtUtil.parseJWT(token);
        String userId = claims.getSubject();
        chkUserCarInfo.setUserId(Integer.valueOf(userId));
        chkUserCarInfo.setCreateTime(new Date());
        chkUserCarInfo.setUpdateTime(new Date());
        //入库
        int insert = carMapper.insert(chkUserCarInfo);
        if (insert > 0 ){
            //入库成功写入缓存
        }

        return new ResponseResult(200,"添加车辆成功");
    }
}
