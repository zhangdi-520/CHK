package com.yunhua.service.impl;

import com.yunhua.constant.RedisConstant;
import com.yunhua.dao.CarDao;
import com.yunhua.domain.ChkUserCarInfo;
import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.requestRo.MerchantInfoRo;
import com.yunhua.golbalexception.vo.ResultEnum;
import com.yunhua.mapper.CarMapper;
import com.yunhua.security.util.JwtUtil;
import com.yunhua.service.CarService;
import com.yunhua.utils.RedisCache;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @version V1.0
 * @program: CHK
 * @description: 车辆服务实现类
 * @author: Mr.Zhang
 * @create: 2022-05-12 09:40
 **/
@Service
@Slf4j
public class CarServiceImpl implements CarService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private CarDao carDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addUserCar(String token, ChkUserCarInfo chkUserCarInfo) throws Exception {
        Claims claims = JwtUtil.parseJWT(token);
        String userId = claims.getSubject();
        chkUserCarInfo.setUserId(Integer.valueOf(userId));
        chkUserCarInfo.setCreateTime(new Date());
        chkUserCarInfo.setUpdateTime(new Date());
        //入库
        int insert = carDao.insert(chkUserCarInfo);
        if (insert > 0 ) {
            //这一步查询是为了刷新车辆缓存
            List<ChkUserCarInfo> allCarInfoByUserId = carDao.findAllCarInfoByUserId(userId);
            return new ResponseResult(ResultEnum.ADDCARSUCCESS.getCode(), ResultEnum.ADDCARSUCCESS.getMsg());
        }
        return new ResponseResult(ResultEnum.ADDCARFAIL.getCode(), ResultEnum.ADDCARFAIL.getMsg());
    }

    @Override
    public ResponseResult getCar(String token) throws Exception {
        Claims claims = JwtUtil.parseJWT(token);
        String userId = claims.getSubject();
        List<ChkUserCarInfo> carInfos = carDao.findAllCarInfoByUserId(userId);
        return new ResponseResult(ResultEnum.GETCARSUCCESS.getCode(),ResultEnum.GETCARSUCCESS.getMsg(),carInfos);
    }

}
