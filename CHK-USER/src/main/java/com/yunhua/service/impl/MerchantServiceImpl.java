package com.yunhua.service.impl;


import com.yunhua.config.BasicConfig;
import com.yunhua.dao.MerchantDao;
import com.yunhua.domain.ChkMerchantInfo;
import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.requestRo.MerchantInfoRo;
import com.yunhua.execption.BusinessException;
import com.yunhua.execption.vo.ResultEnum;
import com.yunhua.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private BasicConfig basicConfig;

    @Autowired
    private MerchantDao merchantDao;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public ResponseResult carList(MerchantInfoRo merchantInfoRo) throws ExecutionException, InterruptedException {
        if (merchantInfoRo.getLatitude() == null || merchantInfoRo.getLongitude() == null){
            throw new BusinessException(ResultEnum.PARAMCHECHFAIL);
        }
        log.info("判断距离是否为空，为商家设置默认距离");
        if (merchantInfoRo.getRadius() == null){
            merchantInfoRo.setRadius(basicConfig.getChkMerchantDistant());
        }
        //异步编排任务
        CompletableFuture<List<ChkMerchantInfo> > future = CompletableFuture.supplyAsync(() -> {
            return merchantDao.getMerchantList(merchantInfoRo);
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
            merchantList = merchantList.stream().sorted(Comparator.comparing(ChkMerchantInfo::getScore)).collect(Collectors.toList());
        }
        return new ResponseResult(200,merchantList);
    }

}
