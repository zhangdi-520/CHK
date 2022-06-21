package com.yunhua.service;

import com.yunhua.entity.vo.MerchantInfoSelectConditionVo;
import com.yunhua.entity.vo.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("chk-merchant") // nacos 服务 id
public interface MerchantFeignService {

    @PostMapping("/chk-merchant/list") // 使用 post 方式，调用商户接口的根据选择条件查询商户的接口
    public ResponseResult listAllMerchantInfoBySelectCondition(@RequestHeader("token") String token, @RequestBody MerchantInfoSelectConditionVo merchantInfoRo);
}
