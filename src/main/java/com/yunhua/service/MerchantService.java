package com.yunhua.service;

import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.requestRo.MerchantInfoRo;

public interface MerchantService {

    ResponseResult carList(MerchantInfoRo merchantInfoRo);
}
