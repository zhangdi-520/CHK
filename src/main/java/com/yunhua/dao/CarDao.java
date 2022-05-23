package com.yunhua.dao;


import com.yunhua.constant.RedisConstant;
import com.yunhua.domain.ChkUserCarInfo;
import com.yunhua.mapper.CarMapper;
import com.yunhua.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Component
public class CarDao {

    @Autowired
    private CarMapper carMapper;

    @Autowired
    private RedisCache redisCache;

    public Integer insert(ChkUserCarInfo chkUserCarInfo){
        int insert = carMapper.insert(chkUserCarInfo);
        if (insert > 0 ){
            //成功插入数据，删除缓存
            redisCache.deleteObject(RedisConstant.CARINFO+chkUserCarInfo.getUserId());
        }
        return insert;
    }


    public List<ChkUserCarInfo> findAllCarInfoByUserId(String userId){
        List<ChkUserCarInfo> cacheList = null;
        cacheList = redisCache.getCacheList(RedisConstant.CARINFO + userId);
        if (CollectionUtils.isEmpty(cacheList) ) {
            cacheList = carMapper.findAllCarInfoByUserId(userId);
            if (!CollectionUtils.isEmpty(cacheList)){
                redisCache.setCacheList(RedisConstant.CARINFO+userId, cacheList, RedisConstant.CARINFOEXPIRE);
            }
        }
        return cacheList;
    }
}
