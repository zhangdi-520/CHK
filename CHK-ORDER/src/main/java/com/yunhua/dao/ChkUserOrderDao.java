package com.yunhua.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunhua.entity.ChkMerchantInfo;
import com.yunhua.entity.ChkUserOrder;
import com.yunhua.mapper.ChkUserOrderMapper;
import io.swagger.annotations.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ChkUserOrderDao {

    @Autowired
    private ChkUserOrderMapper orderMapper;

    public List<ChkUserOrder> listOrderByMerchantList(List<ChkMerchantInfo> merchantList,String orderStatusListStr){

        String[] split = orderStatusListStr.split(",");
        List<String> orderStatusList = Arrays.asList(split);
        List<ChkUserOrder> orderList = orderMapper.listOrderByMerchantList(merchantList,orderStatusList);
        return orderList;
    }

    public List<ChkUserOrder> listOrderByMerchantId(long merchantId,String orderStatusListStr){
        String[] split = orderStatusListStr.split(",");
        List<String> orderStatusList = Arrays.asList(split);
        List<ChkUserOrder> orderList = orderMapper.listOrderByMerchantId(merchantId, orderStatusList);
        return orderList;
    }


}
