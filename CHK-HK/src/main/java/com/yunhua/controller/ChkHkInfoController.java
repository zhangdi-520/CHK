package com.yunhua.controller;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunhua.annotation.WebLogAnnotation;
import com.yunhua.entity.ChkHkInfo;
import com.yunhua.entity.ChkMerchantInfo;
import com.yunhua.entity.ChkUserOrder;
import com.yunhua.entity.vo.MerchantInfoSelectConditionVo;
import com.yunhua.entity.vo.ResponseResult;
import com.yunhua.execption.vo.ResultEnum;
import com.yunhua.service.ChkHkInfoService;
import com.yunhua.service.MerchantFeignService;
import com.yunhua.service.OrderFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 魏启恒
 * @since 2022-06-06
 */
@RestController
@RequestMapping
@Api(value = "管家信息接口",tags = "管家信息")
public class ChkHkInfoController {

    @Autowired
    private ChkHkInfoService chkHkInfoService;

    @Resource
    private MerchantFeignService merchantFeignService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Resource
    private OrderFeignService orderFeignService;

    @GetMapping("/list")
    @ApiOperation("查询所有的管家信息")
    public ResponseResult listAllHkInfo(){
        List<ChkHkInfo> chkHkInfoList = chkHkInfoService.listAllHkInfo();
        if (chkHkInfoList.size() == 0){
            return new ResponseResult(ResultEnum.NOTFINDINDATABASE.getCode(), ResultEnum.NOTFINDINDATABASE.getMsg());
        }
        return new ResponseResult<>(ResultEnum.LISTALLEMPLOYEESUCCESS.getCode(), chkHkInfoList);

    }

    @GetMapping("/get/{hkId}")
    @ApiOperation("根据管家Id查询管家信息")
    @WebLogAnnotation(module = "CHK-HK" , operator = "getHkInfoByHkId")
    public ResponseResult getHkInfoByHkId(@RequestHeader("token") String token,  @PathVariable("hkId") Long hkId){
        ChkHkInfo hkInfo = chkHkInfoService.getHkInfoByHkId(hkId);
        if (hkInfo == null){
            return new ResponseResult(ResultEnum.NOTFINDINDATABASE.getCode(), ResultEnum.NOTFINDINDATABASE.getMsg());
        }
        return new ResponseResult<>(ResultEnum.LISTALLEMPLOYEESUCCESS.getCode(), hkInfo);

    }


    @PostMapping("/add")
    @ApiOperation("添加管家信息")
    public ResponseResult addHkInfo(@RequestBody ChkHkInfo hkInfo) throws ExecutionException, InterruptedException {
        return chkHkInfoService.addHkInfo(hkInfo);
    }

    @DeleteMapping("/delete/{hkId}")
    @ApiOperation("根据管家Id删除管家信息")
    public ResponseResult deleteHkIdByHkId(@PathVariable("hkId") Long hkId) throws ExecutionException, InterruptedException {
        return chkHkInfoService.deleteHkInfoByHkId(hkId);
    }

    @PostMapping("/update/{hkId}")
    @ApiOperation("根据用户Id更新用户信息")
    public ResponseResult updateHkInfoByHkId(@PathVariable("hkId") Long hkId, @RequestBody ChkHkInfo hkInfo) throws ExecutionException, InterruptedException {
        return chkHkInfoService.updateHkInfoByHkId(hkId,hkInfo);
    }

    @PostMapping("/order/list/hkLocation")
    @ApiOperation("根据管家位置查询订单")
    public ResponseResult listOrderByHkLocation(@RequestHeader("token") String token,@RequestBody MerchantInfoSelectConditionVo merchantInfoSelectConditionVo,String orderStatusListStr){

        ResponseResult merchantListResult = merchantFeignService.listAllMerchantInfoBySelectCondition(token, merchantInfoSelectConditionVo);
        JavaType merchantListType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ChkMerchantInfo.class);
        List<ChkMerchantInfo> merchantInfoList = objectMapper.convertValue(merchantListResult.getData(), merchantListType);

        ResponseResult responseResult = orderFeignService.listOrderByMerchantList(merchantInfoList,orderStatusListStr);
        JavaType orderListType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ChkUserOrder.class);
        List<ChkUserOrder> orderList = objectMapper.convertValue(responseResult.getData(), orderListType);
        return new ResponseResult(200,orderList);
    }


    //测试Feign代码
//    @RequestMapping("/call/{name}")
//    public String call(@PathVariable String name) {
//        return LocalTime.now() + "——服务提供者1：" + name;
//    }


}

