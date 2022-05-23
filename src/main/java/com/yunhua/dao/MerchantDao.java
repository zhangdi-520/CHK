package com.yunhua.dao;

import com.yunhua.domain.ChkMerchantInfo;
import com.yunhua.domain.requestRo.MerchantInfoRo;
import com.yunhua.mapper.MerchantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MerchantDao {

    @Autowired
    private MerchantMapper merchantMapper;

    public List<ChkMerchantInfo> getMerchantList(MerchantInfoRo merchantInfoRo){
        List<ChkMerchantInfo> merchantList = merchantMapper.getMerchantList(merchantInfoRo);
        return merchantList;
    }


}
