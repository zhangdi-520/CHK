package com.yunhua.service;

import com.yunhua.entity.ChkMerchantInfo;
import com.yunhua.entity.vo.MerchantInfoSelectConditionVo;
import com.yunhua.entity.vo.ResponseResult;

import java.util.concurrent.ExecutionException;

public interface ChkMerchantInfoService {
    ResponseResult listAllMerchantInfoBySelectCondition(MerchantInfoSelectConditionVo merchantInfoRo) throws ExecutionException, InterruptedException;

    ResponseResult addMerchantInfo(ChkMerchantInfo merchantInfo);

    ResponseResult deleteMerchantInfoByMerchantId(Long merchantId);

    ResponseResult updateMerchantInfoByMerchantId(Long merchantId, ChkMerchantInfo merchantInfo);

    ChkMerchantInfo getMerchantInfoByMerchantId(Long merchantId);
}