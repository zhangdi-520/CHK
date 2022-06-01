package com.yunhua.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yunhua.entity.ChkMerchantInfo;
import com.yunhua.entity.vo.MerchantInfoSelectConditionVo;
import com.yunhua.mapper.ChkMerchantInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChkMerchantInfoDao {
    @Autowired
    private ChkMerchantInfoMapper merchantInfoMapper;

    public List<ChkMerchantInfo> listAllMerchantInfoBySelectCondition(MerchantInfoSelectConditionVo merchantInfoRo){
        List<ChkMerchantInfo> merchantList = merchantInfoMapper.listAllMerchantInfoBySelectCondition(merchantInfoRo);
        return merchantList;

    }

    public int addMerchantInfo(ChkMerchantInfo merchantInfo) {
        int insertStatus = merchantInfoMapper.insert(merchantInfo);
        return insertStatus;
    }

    public int deleteMerchantInfoByMerchantId(Long merchantId) {
        LambdaUpdateWrapper<ChkMerchantInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ChkMerchantInfo::getId,merchantId);
        wrapper.eq(ChkMerchantInfo::getDelFlag,0);
        wrapper.set(ChkMerchantInfo::getDelFlag,1);
        int deleteStatus = merchantInfoMapper.update(null, wrapper);
        return deleteStatus;
    }

    public int updateMerchantInfoByMerchantId(Long merchantId, ChkMerchantInfo merchantInfo) {
        ChkMerchantInfo merchantInfoSelected = merchantInfoMapper.selectById(merchantId);
        merchantInfo.setId(merchantId);
        merchantInfo.setUpdateTime(merchantInfoSelected.getUpdateTime());
        merchantInfo.setScore(merchantInfoSelected.getScore());
        merchantInfo.setCreateTime(merchantInfoSelected.getCreateTime());
        merchantInfo.setDelFlag(merchantInfoSelected.getDelFlag());
        LambdaUpdateWrapper<ChkMerchantInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ChkMerchantInfo::getId,merchantId);
        wrapper.eq(ChkMerchantInfo::getDelFlag,0);
        int updateStatus = merchantInfoMapper.update(merchantInfo,wrapper);
        return updateStatus;
    }

    public ChkMerchantInfo getMerchantInfoByMerchantId(Long merchantId) {
        LambdaQueryWrapper<ChkMerchantInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChkMerchantInfo::getId,merchantId);
        wrapper.eq(ChkMerchantInfo::getDelFlag,0);
        ChkMerchantInfo merchantInfo = merchantInfoMapper.selectOne(wrapper);
        return merchantInfo;
    }
}
