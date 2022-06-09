package com.yunhua.dao;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.yunhua.entity.ChkMerchantEmployee;
import com.yunhua.mapper.ChkMerchantEmployeeMapper;
import com.yunhua.mapper.ChkMerchantInfoMapper;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
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

    public ChkMerchantEmployee selectMerchantEmployeeByEmployeeId(Long employeeId){
        LambdaQueryWrapper<ChkMerchantEmployee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChkMerchantEmployee::getId,employeeId);
        wrapper.eq(ChkMerchantEmployee::getDelFlag,0);
        ChkMerchantEmployee employee = merchantEmployeeMapper.selectOne(wrapper);
        return employee;
    }

    public int updateMerchantEmployeeByEmployeeId(Long employeeId , ChkMerchantEmployee employee){

        ChkMerchantEmployee employeeSelected = merchantEmployeeMapper.selectById(employeeId);
        if (employeeSelected == null){
            return 0;
        }
        employee.setCreateTime(employeeSelected.getCreateTime());
        employee.setUpdateTime(new Date());
        employee.setMerchantId(employeeSelected.getMerchantId());
        employee.setId(employeeSelected.getId());
        employee.setDelFlag(employeeSelected.getDelFlag());
        LambdaUpdateWrapper<ChkMerchantEmployee> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ChkMerchantEmployee::getId,employeeId)
                .eq(ChkMerchantEmployee::getDelFlag,0);
        int updateStatus = merchantEmployeeMapper.update(employee, updateWrapper);
        return updateStatus;
    }

    public int addMerchantEmployee(ChkMerchantEmployee employee){
        employee.setCreateTime(new Date());
        employee.setUpdateTime(new Date());
        employee.setDelFlag(0);
        int insertStatus = merchantEmployeeMapper.insert(employee);
        return insertStatus;
    }

    public int deleteMerchantEmployeeByEmployeeId(Long employeeId){
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), ChkMerchantEmployee.class);
        LambdaUpdateWrapper<ChkMerchantEmployee> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ChkMerchantEmployee::getId,employeeId)
                .eq(ChkMerchantEmployee::getDelFlag,0)
                .set(ChkMerchantEmployee::getDelFlag,1);
        int deleteStatus = merchantEmployeeMapper.update(null, wrapper);
        return deleteStatus;

    }


}
