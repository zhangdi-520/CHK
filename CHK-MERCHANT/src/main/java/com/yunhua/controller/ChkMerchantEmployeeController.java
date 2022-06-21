package com.yunhua.controller;


import com.yunhua.entity.ChkMerchantEmployee;
import com.yunhua.entity.vo.ResponseResult;
import com.yunhua.execption.vo.ResultEnum;
import com.yunhua.service.ChkMerchantEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
@RequestMapping("/employee")
@Api(value = "商家的员工信息接口",tags = "商家员工信息")
public class ChkMerchantEmployeeController {

    @Autowired
    private ChkMerchantEmployeeService merchantEmployeeService;

    @GetMapping("/list/{mid}")
    @ApiOperation("根据商户Id获取该商户的所有员工")
    public ResponseResult listAllMerchantEmployeeByMerchantId(@PathVariable("mid") Long merchantId) throws ExecutionException, InterruptedException {
        List<ChkMerchantEmployee> employeeList = merchantEmployeeService.listAllMerchantEmployeeByMerchantId(merchantId);
        if (employeeList.size() == 0){
            return new ResponseResult(ResultEnum.NOTFINDINDATABASE.getCode(), ResultEnum.NOTFINDINDATABASE.getMsg());
        }
        return new ResponseResult<>(ResultEnum.LISTALLEMPLOYEESUCCESS.getCode(), employeeList);
    }


    @GetMapping("/get/{eid}")
    @ApiOperation("根据员工Id去查询单个员工")
    public ResponseResult getMerchantEmployeeByEmployeeId(@PathVariable("eid") Long employeeId) throws ExecutionException, InterruptedException {
        ChkMerchantEmployee employee = merchantEmployeeService.getMerchantEmployeeByEmployeeId(employeeId);

        if (employee == null){
            return new ResponseResult(ResultEnum.NOTFINDINDATABASE.getCode(), ResultEnum.NOTFINDINDATABASE.getMsg());
        }

        return new ResponseResult<>(ResultEnum.LISTALLEMPLOYEESUCCESS.getCode(), employee);
    }

    @PostMapping("/add")
    @ApiOperation("添加员工信息")
    public ResponseResult addMerchantEmployee(@RequestBody ChkMerchantEmployee employee) throws ExecutionException, InterruptedException {
        return merchantEmployeeService.addMerchantEmployee(employee);
    }

    @DeleteMapping("/delete/{eid}/{mid}")
    @ApiOperation("根据员工Id删除员工信息")
    public ResponseResult deleteMerchantEmployeeByEmployeeId(@PathVariable("eid") Long employeeId, @PathVariable("mid") Long merchantId) throws ExecutionException, InterruptedException {
        return merchantEmployeeService.deleteMerchantEmployeeByEmployeeId(employeeId,merchantId);
    }


    @PostMapping("/update/{eid}/{mid}")
    @ApiOperation("根据员工Id更新员工信息")
    public ResponseResult updateMerchantEmployeeByEmployeeId(@PathVariable("eid") Long employeeId,@PathVariable("mid") Long merchantId, @RequestBody ChkMerchantEmployee employee) throws ExecutionException, InterruptedException {
        return merchantEmployeeService.updateMerchantEmployeeByEmployeeId(employeeId,merchantId,employee);
    }

}

