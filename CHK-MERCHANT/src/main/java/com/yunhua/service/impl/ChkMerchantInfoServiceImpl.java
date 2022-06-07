package com.yunhua.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yunhua.annotation.RedissonReadLock;
import com.yunhua.annotation.RedissonWriteLock;
import com.yunhua.config.BasicConfig;
import com.yunhua.constant.LockConstant;
import com.yunhua.constant.RedisConstant;
import com.yunhua.dao.ChkMerchantInfoDao;
import com.yunhua.entity.ChkMerchantInfo;
import com.yunhua.entity.vo.MerchantInfoSelectConditionVo;
import com.yunhua.entity.vo.ResponseResult;
import com.yunhua.golbalexception.exception.BusinessException;
import com.yunhua.golbalexception.vo.ResultEnum;
import com.yunhua.mapper.ChkMerchantInfoMapper;
import com.yunhua.service.ChkMerchantInfoService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChkMerchantInfoServiceImpl implements ChkMerchantInfoService {

    @Autowired
    private BasicConfig basicConfig;

    @Autowired
    private ChkMerchantInfoDao merchantInfoDao;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private ChkMerchantInfoMapper merchantInfoMapper;

    /**
     *
     * 根据地理位置信息或者评分信息（可选）来获取所有列表
     *
     * @param merchantInfoRo
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */

    @Override
    @RedissonReadLock(value = LockConstant.MERCHANT+"listAllMerchantInfo")
    public ResponseResult listAllMerchantInfoBySelectCondition(MerchantInfoSelectConditionVo merchantInfoRo) throws ExecutionException, InterruptedException {
        if (merchantInfoRo.getLatitude() == null || merchantInfoRo.getLongitude() == null){
            throw new BusinessException(ResultEnum.PARAMCHECHFAIL);
        }
        log.info("判断距离是否为空，为商家设置默认距离");
        if (merchantInfoRo.getRadius() == null){
            merchantInfoRo.setRadius(basicConfig.getChkMerchantDistant());
        }
        //异步编排任务
        CompletableFuture<List<ChkMerchantInfo>> future = CompletableFuture.supplyAsync(() -> {
            return merchantInfoDao.listAllMerchantInfoBySelectCondition(merchantInfoRo);
        },threadPoolTaskExecutor);
        future.thenAcceptAsync(res->{
            log.info("查询到的商家信息{}",res);
            res.forEach(x->{
                //TODO 这里根据没个商家的id到订单表查处单量然后放入实体中,做的时候顺便测试一下是不是使用自定义的线程池
            });
        },threadPoolTaskExecutor);
        List<ChkMerchantInfo> merchantList  = future.get();
        if (merchantInfoRo.getType() == 2){
            log.info("按照评分筛选商家");
            merchantList = merchantList.stream().sorted(Comparator.comparing(ChkMerchantInfo::getScore, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return -Integer.compare(o1,o2);
                }
            })).collect(Collectors.toList());
        }
        return new ResponseResult(200,merchantList);
    }


    /**
     * 插入商户信息
     * @param merchantInfo
     * @return
     */
    @Override
    //@CachePut(value = RedisConstant.MERCHANTINFO,key = "#merchantInfo.id")
    //@RedissonReadLock(value = LockConstant.USER+"#mobile")
    @Transactional(rollbackFor = Exception.class )
    public ResponseResult addMerchantInfo(ChkMerchantInfo merchantInfo) {

        int insertStatus = merchantInfoDao.addMerchantInfo(merchantInfo);
        if (insertStatus == 0){
            return new ResponseResult(ResultEnum.ADDDATABASEFAIL.getCode(), ResultEnum.ADDDATABASEFAIL.getMsg());
        }
        return new ResponseResult(200,merchantInfo);

    }

    /**
     * 根据商户Id删除商户信息
     * @param merchantId
     * @return
     */

    @Override
    @CacheEvict(value = RedisConstant.MERCHANTINFO,key = "#merchantId")
    @RedissonWriteLock(value = LockConstant.MERCHANT+"#merchantId")
    @Transactional(rollbackFor = Exception.class )
    public ResponseResult deleteMerchantInfoByMerchantId(Long merchantId) {
        int deleteStatus = merchantInfoDao.deleteMerchantInfoByMerchantId(merchantId);
        if (deleteStatus == 0){
            return new ResponseResult(ResultEnum.NOTFINDINDATABASE.getCode(), ResultEnum.NOTFINDINDATABASE.getMsg());
        }
        return new ResponseResult(ResultEnum.DELETEMERCHANTSUCCESS.getCode(),null);
    }


    /**
     * 根据商户Id更新商户信息
     * @param merchantId
     * @param merchantInfo
     * @return
     */
    @Override
    @CacheEvict(value = RedisConstant.MERCHANTINFO,key = "#merchantId")
    @RedissonWriteLock(value = LockConstant.MERCHANT+"#merchantId")
    @Transactional(rollbackFor = Exception.class )
    public ResponseResult updateMerchantInfoByMerchantId(Long merchantId, ChkMerchantInfo merchantInfo) {
        int updateStatus = merchantInfoDao.updateMerchantInfoByMerchantId(merchantId, merchantInfo);


        LambdaUpdateWrapper<ChkMerchantInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ChkMerchantInfo::getId,merchantId)
                .set(ChkMerchantInfo::getScore,100);
        merchantInfoMapper.update(null,wrapper);

        if (updateStatus == 0){
            return new ResponseResult(ResultEnum.NOTFINDINDATABASE.getCode(), ResultEnum.NOTFINDINDATABASE.getMsg());
        }
        return new ResponseResult(ResultEnum.UPDATEMERCHANTSUCCESS.getCode(), merchantInfo);

    }

    /**
     * 根据商户Id获取商户信息
     * @param merchantId
     * @return
     */
    @Override
    @RedissonReadLock(value = LockConstant.MERCHANT+"#merchantId")
    @Cacheable(value = {RedisConstant.MERCHANTINFO} ,key = "#merchantId" ,sync = true )

    public ChkMerchantInfo getMerchantInfoByMerchantId(Long merchantId) {
        ChkMerchantInfo merchantInfo = merchantInfoDao.getMerchantInfoByMerchantId(merchantId);

        return merchantInfo;

    }


}
