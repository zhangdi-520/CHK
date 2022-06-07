package com.yunhua.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yunhua.entity.ChkHkInfo;
import com.yunhua.mapper.ChkHkInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ChkHkInfoDao {


    @Autowired
    private ChkHkInfoMapper chkHkInfoMapper;

    public List<ChkHkInfo> listAllHkInfo(){

        LambdaQueryWrapper<ChkHkInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChkHkInfo::getDelFlag,0);
        wrapper.orderByDesc(ChkHkInfo::getSatisfaction);
        List<ChkHkInfo> chkHkInfoList = chkHkInfoMapper.selectList(wrapper);
        return chkHkInfoList;

    }

    public ChkHkInfo getHkInfoByHkId(Long hkId){

        LambdaQueryWrapper<ChkHkInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChkHkInfo::getDelFlag,0)
                .eq(ChkHkInfo::getId,hkId);
        ChkHkInfo chkHkInfo = chkHkInfoMapper.selectOne(wrapper);
        return chkHkInfo;

    }

    public int addHkInfo(ChkHkInfo hkInfo){
        hkInfo.setCreateTime(new Date());
        hkInfo.setUpdateTime(new Date());
        hkInfo.setSatisfaction(60);
        hkInfo.setDelFlag(0);
        int insertStatus = chkHkInfoMapper.insert(hkInfo);
        return insertStatus;
    }

    public int deleteHkInfoByHkId(Long hkId){
        LambdaUpdateWrapper<ChkHkInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ChkHkInfo::getId,hkId)
                .eq(ChkHkInfo::getDelFlag,0)
                .set(ChkHkInfo::getDelFlag,1);
        int deleteStatus = chkHkInfoMapper.update(null, wrapper);
        return deleteStatus;

    }

    public int updateHkInfoByHkId(Long hkId,ChkHkInfo hkInfo){
        LambdaQueryWrapper<ChkHkInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChkHkInfo::getId,hkId)
                .eq(ChkHkInfo::getDelFlag,0);
        ChkHkInfo hkInfoSelected = chkHkInfoMapper.selectOne(queryWrapper);
        if (hkInfoSelected == null){
            return 0;
        }
        hkInfo.setCreateTime(hkInfoSelected.getCreateTime());
        hkInfo.setId(hkInfoSelected.getId());
        hkInfo.setUpdateTime(new Date());
        hkInfo.setDelFlag(hkInfoSelected.getDelFlag());
        hkInfo.setSatisfaction(hkInfoSelected.getSatisfaction());
        int updateStatus = chkHkInfoMapper.updateById(hkInfo);

        return updateStatus;

    }

}
