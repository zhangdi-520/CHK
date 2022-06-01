package com.yunhua.service.impl;

import com.yunhua.annotation.RedissonReadLock;
import com.yunhua.constant.LockConstant;
import com.yunhua.constant.RedisConstant;
import com.yunhua.dao.ChkMerchantEmployeeDao;
import com.yunhua.entity.ChkMerchantEmployee;
import com.yunhua.entity.vo.ResponseResult;
import com.yunhua.golbalexception.vo.ResultEnum;
import com.yunhua.mapper.ChkMerchantEmployeeMapper;
import com.yunhua.service.ChkMerchantEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 魏启恒
 * @since 2022-05-31
 */
@Service
public class ChkMerchantEmployeeServiceImpl extends ServiceImpl<ChkMerchantEmployeeMapper, ChkMerchantEmployee> implements ChkMerchantEmployeeService {

    @Autowired
    private ChkMerchantEmployeeDao merchantEmployeeDao;

    /**
     * 根据商户Id查询这个商户下的所有员工
     * @param merchantId
     * @return
     */

    @Override
    @RedissonReadLock(value = LockConstant.EMPLOYEE+"listAllEmployeeInfo")
    @Cacheable(value = {RedisConstant.EMPLOYEEINFO+"ALLEMPLOYEE"} ,key = "#merchantId" ,sync = true)
    public List<ChkMerchantEmployee> listAllMerchantEmployeeByMerchantId(Long merchantId) {
        List<ChkMerchantEmployee> employeeList = merchantEmployeeDao.listAllMerchantEmployeeByMerchantId(merchantId);
        return employeeList;
    }
}
