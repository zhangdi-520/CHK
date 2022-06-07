package com.yunhua.service;

import com.yunhua.entity.ChkMerchantEmployee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunhua.entity.vo.ResponseResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 魏启恒
 * @since 2022-05-31
 */

public interface ChkMerchantEmployeeService extends IService<ChkMerchantEmployee> {

    List<ChkMerchantEmployee> listAllMerchantEmployeeByMerchantId(Long merchantId);
    ChkMerchantEmployee getMerchantEmployeeByEmployeeId(Long employeeId);
    ResponseResult addMerchantEmployee(ChkMerchantEmployee employee);
    ResponseResult deleteMerchantEmployeeByEmployeeId(Long employeeId,Long merchantId);
    ResponseResult updateMerchantEmployeeByEmployeeId(Long employeeId,Long merchantId,ChkMerchantEmployee employee);
}
