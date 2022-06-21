package com.yunhua.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunhua.dao.ChkUserOrderDao;
import com.yunhua.entity.ChkMerchantInfo;
import com.yunhua.entity.ChkUserOrder;
import com.yunhua.mapper.ChkUserOrderMapper;
import com.yunhua.service.ChkUserOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 魏启恒
 * @since 2022-06-15
 */
@Service
public class ChkUserOrderServiceImpl extends ServiceImpl<ChkUserOrderMapper, ChkUserOrder> implements ChkUserOrderService {


    @Autowired
    private ChkUserOrderDao orderDao;



    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 根据商家列表查询这些商家的订单/
     * 此时并没有考虑订单状态
     * @param merchantList
     * @return
     */

    @Override
    public List<ChkUserOrder> listOrderByMerchantList(List<ChkMerchantInfo> merchantList, String orderStatusListStr) {
        List<ChkUserOrder> orderList = orderDao.listOrderByMerchantList(merchantList,orderStatusListStr);
        return orderList;
    }

    @Override
    public List<ChkUserOrder> listOrderByMerchantId(long merchantId, String orderStatusListStr) {
        List<ChkUserOrder> orderList = orderDao.listOrderByMerchantId(merchantId, orderStatusListStr);
        return orderList;
    }


}
