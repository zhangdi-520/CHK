package com.yunhua.service.impl;

import com.yunhua.annotation.MyRedissonReadLock;
import com.yunhua.annotation.MyRedissonWriteLock;
import com.yunhua.annotation.ReadOnly;
import com.yunhua.constant.LockConstant;
import com.yunhua.constant.RedisConstant;
import com.yunhua.dao.ChkHkInfoDao;
import com.yunhua.entity.ChkHkInfo;
import com.yunhua.entity.vo.ResponseResult;
import com.yunhua.execption.vo.ResultEnum;
import com.yunhua.mapper.ChkHkInfoMapper;
import com.yunhua.service.ChkHkInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 魏启恒
 * @since 2022-06-06
 */
@Service
public class ChkHkInfoServiceImpl extends ServiceImpl<ChkHkInfoMapper, ChkHkInfo> implements ChkHkInfoService {

    @Autowired
    private ChkHkInfoDao chkHkInfoDao;

    @Override
    @MyRedissonReadLock(value = LockConstant.HK + "ALLHKINFO")
    @Cacheable(value = {RedisConstant.HKINFO} ,key = "'ALLHKINFO'", sync = true)
    @ReadOnly
    public List<ChkHkInfo> listAllHkInfo() {
        List<ChkHkInfo> hkInfoList = chkHkInfoDao.listAllHkInfo();
        return hkInfoList;
    }

    @Override
    @MyRedissonReadLock(value = LockConstant.HK + "#hkId")
    @Cacheable(value = {RedisConstant.HKINFO}, key = "#hkId", sync = true)
    @ReadOnly
    public ChkHkInfo getHkInfoByHkId(Long hkId) {
        ChkHkInfo hkInfoByHkId = chkHkInfoDao.getHkInfoByHkId(hkId);
        return hkInfoByHkId;
    }



    /**
     * 插入管家信息
     * @param hkInfo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class )
    //@CachePut(value = RedisConstant.MERCHANTINFO,key = "#merchantInfo.id")
    //@RedissonReadLock(value = LockConstant.USER+"#mobile")
    public ResponseResult addHkInfo(ChkHkInfo hkInfo) {

        int insertStatus = chkHkInfoDao.addHkInfo(hkInfo);
        if (insertStatus == 0){
            return new ResponseResult(ResultEnum.ADDDATABASEFAIL.getCode(), ResultEnum.ADDDATABASEFAIL.getMsg());
        }
        return new ResponseResult(200,hkInfo);

    }

    /**
     * 根据商户Id删除商户信息
     * @param hkId
     * @return
     */

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = {RedisConstant.HKINFO},key = "#hkId"),
                    @CacheEvict(value = {RedisConstant.HKINFO},key = "'ALLHKINFO'")
            }
    )
    @MyRedissonWriteLock(value = LockConstant.HK+"#hkId")
    @Transactional(rollbackFor = Exception.class )
    public ResponseResult deleteHkInfoByHkId(Long hkId) {
        int deleteStatus = chkHkInfoDao.deleteHkInfoByHkId(hkId);

        if (deleteStatus == 0){
            return new ResponseResult(ResultEnum.NOTFINDINDATABASE.getCode(), ResultEnum.NOTFINDINDATABASE.getMsg());
        }
        return new ResponseResult(ResultEnum.DELETEMERCHANTSUCCESS.getCode(),null);
    }


    /**
     * 根据管家Id去修改管家的信息
     * @param hkId
     * @param hkInfo
     * @return
     */

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = {RedisConstant.HKINFO},key = "#hkId"),
                    @CacheEvict(value = {RedisConstant.HKINFO},key = "'ALLHKINFO'")
            }
    )
    @MyRedissonWriteLock(value = LockConstant.HK+"#hkId")
    @Transactional(rollbackFor = Exception.class )
    public ResponseResult updateHkInfoByHkId(Long hkId, ChkHkInfo hkInfo) {
        int updateStatus = chkHkInfoDao.updateHkInfoByHkId(hkId, hkInfo);
        if (updateStatus == 0){
            return new ResponseResult(ResultEnum.NOTFINDINDATABASE.getCode(), ResultEnum.NOTFINDINDATABASE.getMsg());
        }
        return new ResponseResult<>(200,hkInfo);
    }

}
