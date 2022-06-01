package com.yunhua.controller;


import com.yunhua.entity.ChkMerchantEmployee;
import com.yunhua.entity.vo.MerchantInfoSelectConditionVo;
import com.yunhua.entity.vo.ResponseResult;
import com.yunhua.golbalexception.vo.ResultEnum;
import com.yunhua.service.ChkMerchantEmployeeService;
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
 * @since 2022-05-31
 */
@RestController
@RequestMapping("/merchantEmployee")
@Api(value = "商家的员工信息接口",tags = "商家员工信息")
public class ChkMerchantEmployeeController {

    @Autowired
    private ChkMerchantEmployeeService merchantEmployeeService;

    @PostMapping("/list/{mid}")
    @ApiOperation("根据商户Id获取该商户的所有员工")
    public ResponseResult listAllMerchantEmployeeByMerchantId(@PathVariable("mid") Long merchantId) throws ExecutionException, InterruptedException {
        List<ChkMerchantEmployee> employeeList = merchantEmployeeService.listAllMerchantEmployeeByMerchantId(merchantId);

        return new ResponseResult<>(ResultEnum.LISTALLEMPLOYEESUCCESS.getCode(), employeeList);
    }

}

