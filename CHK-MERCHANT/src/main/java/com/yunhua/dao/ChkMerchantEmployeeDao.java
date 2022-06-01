package com.yunhua.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunhua.entity.ChkMerchantEmployee;
import com.yunhua.mapper.ChkMerchantEmployeeMapper;
import com.yunhua.mapper.ChkMerchantInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChkMerchantEmployeeDao {
    @Autowired
    private ChkMerchantEmployeeMapper merchantEmployeeMapper;


    public List<ChkMerchantEmployee> listAllMerchantEmployeeByMerchantId(Long merchantId) {
        LambdaQueryWrapper<ChkMerchantEmployee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChkMerchantEmployee::getMerchantId,merchantId);
        wrapper.eq(ChkMerchantEmployee::getDelFlag,0);
        List<ChkMerchantEmployee> employeeList = merchantEmployeeMapper.selectList(wrapper);
        return employeeList;
    }
}
