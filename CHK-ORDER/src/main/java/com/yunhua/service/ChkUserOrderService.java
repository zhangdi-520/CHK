package com.yunhua.service;

import com.yunhua.entity.ChkMerchantInfo;
import com.yunhua.entity.ChkUserOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunhua.entity.vo.MerchantInfoSelectConditionVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 魏启恒
 * @since 2022-06-15
 */
public interface ChkUserOrderService extends IService<ChkUserOrder> {
    public List<ChkUserOrder> listOrderByMerchantList(List<ChkMerchantInfo> merchantList,String orderStatusList);
    public List<ChkUserOrder> listOrderByMerchantId(long merchantId, String orderStatusListStr);

}
