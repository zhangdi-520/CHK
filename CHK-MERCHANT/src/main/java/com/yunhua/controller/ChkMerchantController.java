package com.yunhua.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunhua.annotation.WebLogAnnotation;
import com.yunhua.entity.ChkMerchantInfo;
import com.yunhua.entity.ChkUserOrder;
import com.yunhua.entity.vo.MerchantInfoSelectConditionVo;
import com.yunhua.entity.vo.ResponseResult;
import com.yunhua.execption.vo.ResultEnum;
import com.yunhua.service.ChkMerchantInfoService;
import com.yunhua.service.OrderFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping
@Api(value = "商家信息接口",tags = "商家信息")
public class ChkMerchantController {

    @Autowired
    private ChkMerchantInfoService merchantInfoService;

    @Resource
    private OrderFeignService orderFeignService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/list")
    @ApiOperation("根据地理位置或者评分等条件查询所有商家")
    @WebLogAnnotation(module = "CHK-MERCHANT" , operator = "listAllMerchantInfoBySelectCondition")
    public ResponseResult listAllMerchantInfoBySelectCondition(@RequestHeader("token") String token,@RequestBody MerchantInfoSelectConditionVo merchantInfoRo) throws ExecutionException, InterruptedException {
        return merchantInfoService.listAllMerchantInfoBySelectCondition(merchantInfoRo);
    }

    @PostMapping("/add")
    @ApiOperation("添加商户信息")
    public ResponseResult addMerchantInfo(@RequestBody ChkMerchantInfo merchantInfo) throws ExecutionException, InterruptedException {
        return merchantInfoService.addMerchantInfo(merchantInfo);
    }

    @DeleteMapping("/delete/{mid}")
    @ApiOperation("根据商户Id删除商户信息")
    public ResponseResult deleteMerchantInfoByMerchantId(@PathVariable("mid") Long merchantId) throws ExecutionException, InterruptedException {
        return merchantInfoService.deleteMerchantInfoByMerchantId(merchantId);
    }

    @PostMapping("/update/{mid}")
    @ApiOperation("根据商户Id更新商户信息")
    public ResponseResult updateMerchantInfoByMerchantId(@PathVariable("mid") Long merchantId,@RequestBody ChkMerchantInfo merchantInfo) throws ExecutionException, InterruptedException {
        return merchantInfoService.updateMerchantInfoByMerchantId(merchantId,merchantInfo);
    }

    @GetMapping("/get/{mid}")
    @ApiOperation("根据商户Id获取商户信息")
    public ResponseResult getMerchantInfoByMerchantId(@PathVariable("mid") Long merchantId) throws ExecutionException, InterruptedException {
        ChkMerchantInfo merchantInfo = merchantInfoService.getMerchantInfoByMerchantId(merchantId);

        if (merchantInfo == null){
            return new ResponseResult(ResultEnum.NOTFINDINDATABASE.getCode(), ResultEnum.NOTFINDINDATABASE.getMsg());
        }

        return new ResponseResult(ResultEnum.GETMERCHANTSUCCESS.getCode(), merchantInfo);
    }

    @GetMapping("order/list/merchantId{mid}")
    @ApiOperation("根据商户Id获取商户的订单")
    public ResponseResult listOrderByMerchantId(@PathVariable("mid") Long merchantId, String orderStatusListStr){
        ResponseResult responseResult = orderFeignService.listOrderByMerchantId(merchantId, orderStatusListStr);
        JavaType orderListType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ChkUserOrder.class);
        List<ChkUserOrder> orderList = objectMapper.convertValue(responseResult.getData(), orderListType);
        return new ResponseResult(200,orderList);
    }

//    @GetMapping("/order/list/mid/{mid}")
//    public ResponseResult listOrderByMerchantId(@PathVariable("mid") Long merchantId){
//
//    }


}