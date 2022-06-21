package com.yunhua.service;

import com.yunhua.entity.ChkMerchantInfo;
import com.yunhua.entity.vo.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("chk-order") // nacos 服务 id
public interface OrderFeignService {

    //调用订单模块的根据商户Id查询所有订单的接口
    @GetMapping("/chk-order/list/merchantId/{mid}")
    public ResponseResult listOrderByMerchantId(@PathVariable("mid") Long merchantId, @RequestParam("orderStatusListStr") String orderStatusListStr);
}
