package com.yunhua.mapper;

import com.yunhua.entity.ChkMerchantInfo;
import com.yunhua.entity.ChkUserOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 魏启恒
 * @since 2022-06-15
 */
@Repository
public interface ChkUserOrderMapper extends BaseMapper<ChkUserOrder> {

    List<ChkUserOrder> listOrderByMerchantList(@Param("merchantList") List<ChkMerchantInfo> merchantList,@Param("orderStatusList") List<String> orderStatusList);

    List<ChkUserOrder> listOrderByMerchantId(@Param("merchantId") long merchantId, @Param("orderStatusList") List<String> orderStatusList);
}
