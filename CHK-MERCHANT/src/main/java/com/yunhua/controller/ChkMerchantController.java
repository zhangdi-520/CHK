package com.yunhua.controller;

import com.yunhua.entity.ChkMerchantInfo;
import com.yunhua.entity.vo.MerchantInfoSelectConditionVo;
import com.yunhua.entity.vo.ResponseResult;
import com.yunhua.golbalexception.vo.ResultEnum;
import com.yunhua.service.ChkMerchantInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/merchant")
@Api(value = "商家信息接口",tags = "商家信息")
public class ChkMerchantController {

    @Autowired
    private ChkMerchantInfoService merchantInfoService;

    @PostMapping("/list")
    @ApiOperation("根据地理位置或者评分等条件查询所有商家")
    public ResponseResult listAllMerchantInfoBySelectCondition(@RequestBody MerchantInfoSelectConditionVo merchantInfoRo) throws ExecutionException, InterruptedException {
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



}