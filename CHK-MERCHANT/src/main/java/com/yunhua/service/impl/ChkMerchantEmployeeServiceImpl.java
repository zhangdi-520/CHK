package com.yunhua.service.impl;

import com.yunhua.annotation.MyRedissonReadLock;
import com.yunhua.annotation.MyRedissonWriteLock;
import com.yunhua.constant.LockConstant;
import com.yunhua.constant.RedisConstant;
import com.yunhua.dao.ChkMerchantEmployeeDao;
import com.yunhua.entity.ChkMerchantEmployee;
import com.yunhua.entity.vo.ResponseResult;
import com.yunhua.execption.vo.ResultEnum;
import com.yunhua.mapper.ChkMerchantEmployeeMapper;
import com.yunhua.service.ChkMerchantEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @MyRedissonReadLock(value = LockConstant.EMPLOYEE+"LISTALLEMPLOYEEINFO")
    @Cacheable(value = {RedisConstant.EMPLOYEEINFO+"ALLEMPLOYEE"} ,key = "#merchantId" ,sync = true)
    public List<ChkMerchantEmployee> listAllMerchantEmployeeByMerchantId(Long merchantId) {
        List<ChkMerchantEmployee> employeeList = merchantEmployeeDao.listAllMerchantEmployeeByMerchantId(merchantId);
        return employeeList;
    }


    /**
     * 根据员工ID查询这个员工
     * @param employeeId
     * @return
     */
    @Override
    @MyRedissonReadLock(value = LockConstant.EMPLOYEE+"#employeeId")
    @Cacheable(value = {RedisConstant.EMPLOYEEINFO} ,key = "#employeeId" ,sync = true)
    public ChkMerchantEmployee getMerchantEmployeeByEmployeeId(Long employeeId) {
        ChkMerchantEmployee employee = merchantEmployeeDao.selectMerchantEmployeeByEmployeeId(employeeId);
        return employee;

    }

    /**
     * 添加员工信息
     * @param employee
     * @return
     */

    @Override
    @Transactional(rollbackFor = Exception.class )
    public ResponseResult addMerchantEmployee(ChkMerchantEmployee employee) {
        int insertStatus = merchantEmployeeDao.addMerchantEmployee(employee);
        if (insertStatus == 0){
            return new ResponseResult(ResultEnum.ADDDATABASEFAIL.getCode(), ResultEnum.ADDDATABASEFAIL.getMsg());
        }
        return new ResponseResult(200,employee);
    }


    /**
     * 根据员工Id删除员工信息
     * @param employeeId
     * @param merchantId
     * @return
     */

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = {RedisConstant.EMPLOYEEINFO} ,key = "#employeeId"),
                    @CacheEvict(value = {RedisConstant.EMPLOYEEINFO+"ALLEMPLOYEE"} ,key = "#merchantId" )
            }
    )
    @Transactional(rollbackFor = Exception.class )
    @MyRedissonWriteLock(value = LockConstant.EMPLOYEE+"#employeeId")
    public ResponseResult deleteMerchantEmployeeByEmployeeId(Long employeeId,Long merchantId) {
        int deleteStatus = merchantEmployeeDao.deleteMerchantEmployeeByEmployeeId(employeeId);
        if (deleteStatus == 0){
            return new ResponseResult(ResultEnum.NOTFINDINDATABASE.getCode(), ResultEnum.NOTFINDINDATABASE.getMsg());
        }
        return new ResponseResult(200,null);
    }

    /**
     *
     * 根据用户Id去修改用户信息
     * @param employeeId
     * @param employee
     * @return
     */

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = {RedisConstant.EMPLOYEEINFO} ,key = "#employeeId"),
                    @CacheEvict(value = {RedisConstant.EMPLOYEEINFO+"ALLEMPLOYEE"} ,key = "#merchantId" )
            }
    )
    @Transactional(rollbackFor = Exception.class )
    @MyRedissonWriteLock(value = LockConstant.EMPLOYEE+"#employeeId")
    public ResponseResult updateMerchantEmployeeByEmployeeId(Long employeeId,Long merchantId,ChkMerchantEmployee employee) {
        int updateStatus = merchantEmployeeDao.updateMerchantEmployeeByEmployeeId(employeeId, employee);
        if (updateStatus == 0){
            return new ResponseResult(ResultEnum.NOTFINDINDATABASE.getCode(), ResultEnum.NOTFINDINDATABASE.getMsg());
        }
        return new ResponseResult<>(200,employee);
    }



}
