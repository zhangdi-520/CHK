package com.yunhua.service;

import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.requestRo.MerchantInfoRo;

import java.util.concurrent.ExecutionException;

public interface MerchantService {

    ResponseResult carList(MerchantInfoRo merchantInfoRo) throws ExecutionException, InterruptedException;
}
