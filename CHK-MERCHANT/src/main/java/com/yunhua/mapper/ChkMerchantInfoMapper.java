package com.yunhua.mapper;

import com.yunhua.entity.ChkMerchantInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunhua.entity.vo.MerchantInfoSelectConditionVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 魏启恒
 * @since 2022-05-31
 */
@Repository
public interface ChkMerchantInfoMapper extends BaseMapper<ChkMerchantInfo> {

    @Select("<script>" +
            "SELECT\n" +
            "        id, merchant_name, address, score, " +
            "        ROUND(\n" +
            "        6371.393 * acos(\n" +
            "        cos(radians(#{latitude,jdbcType=DECIMAL})) * cos(radians(latitude)) * cos(\n" +
            "        radians(longitude) - radians(#{longitude,jdbcType=DECIMAL})\n" +
            "        ) + sin(radians(#{latitude,jdbcType=DECIMAL})) * sin(radians(latitude))\n" +
            "        ),\n" +
            "        3\n" +
            "        ) AS distance\n" +
            "        FROM\n" +
            "        chk_merchant_info\n" +
            "        WHERE \n" +
            "        ROUND(\n" +
            "        6371.393 * acos(\n" +
            "        cos(radians(#{latitude,jdbcType=DECIMAL})) * cos(radians(latitude)) * cos(\n" +
            "        radians(longitude) - radians(#{longitude,jdbcType=DECIMAL})\n" +
            "        ) + sin(radians(#{latitude,jdbcType=DECIMAL})) * sin(radians(latitude))\n" +
            "        ),\n" +
            "        3\n" +
            "        ) &lt; #{radius,jdbcType=DECIMAL} and del_flag = 0\n" +
            "        ORDER BY\n" +
            "            distance" +
            "</script>")
    List<ChkMerchantInfo> listAllMerchantInfoBySelectCondition(MerchantInfoSelectConditionVo merchantInfoRo);
}
