package com.yunhua.controller;


import com.yunhua.entity.ChkHkInfo;
import com.yunhua.entity.vo.ResponseResult;
import com.yunhua.execption.vo.ResultEnum;
import com.yunhua.service.ChkHkInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

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
@RequestMapping("/chkHkInfo")
@Api(value = "管家信息接口",tags = "管家信息")
public class ChkHkInfoController {

    @Autowired
    private ChkHkInfoService chkHkInfoService;

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
    public ResponseResult getHkInfoByHkId(@PathVariable("hkId") Long hkId){
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

}

