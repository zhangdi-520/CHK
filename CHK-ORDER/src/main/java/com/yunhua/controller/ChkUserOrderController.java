package com.yunhua.controller;


import com.yunhua.entity.ChkMerchantInfo;
import com.yunhua.entity.ChkUserOrder;
import com.yunhua.entity.vo.ResponseResult;
import com.yunhua.service.ChkUserOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 魏启恒
 * @since 2022-06-15
 */
@RestController
@RequestMapping
@Api(value = "用户订单接口",tags = "用户订单信息")
public class ChkUserOrderController {

    @Autowired
    private ChkUserOrderService orderService;

//    @GetMapping("/consumer")
//    public String consumer(@RequestParam String name) {
//        // 向调用本地方法一样，调用 openfeign client 中的方法
//        return merchantFeignService.call(name);
//    }

    @PostMapping("list/merchants")
    @ApiOperation("根据商户列表查询订单")
    public ResponseResult listOrderByMerchantList(@RequestBody List<ChkMerchantInfo> merchantInfoList, String orderStatusListStr){
        List<ChkUserOrder> orderList = orderService.listOrderByMerchantList(merchantInfoList,orderStatusListStr);
        return new ResponseResult(200,orderList);
    }
    @ApiOperation("根据商户Id查询所有订单")
    @GetMapping("/list/merchantId/{mid}")
    public ResponseResult listOrderByMerchantId(@PathVariable("mid") Long merchantId, @RequestParam("orderStatusListStr") String orderStatusListStr){
        List<ChkUserOrder> orderList = orderService.listOrderByMerchantId(merchantId, orderStatusListStr);
        return new ResponseResult(200,orderList);
    }

}

