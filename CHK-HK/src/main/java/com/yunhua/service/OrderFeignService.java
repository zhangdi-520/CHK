package com.yunhua.service;

import com.yunhua.entity.ChkMerchantInfo;
import com.yunhua.entity.ChkUserOrder;
import com.yunhua.entity.vo.MerchantInfoSelectConditionVo;
import com.yunhua.entity.vo.ResponseResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("chk-order") // nacos 服务 id
public interface OrderFeignService {

    @PostMapping("/chk-order/list/merchants") // 使用 post 方式，调用订单模块的根据商户列表查询订单的接口
    public ResponseResult listOrderByMerchantList(@RequestBody List<ChkMerchantInfo> merchantInfoList,@RequestParam("orderStatusListStr") String orderStatusListStr);
}
