package com.yunhua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunhua.domain.ChkMerchantInfo;
import com.yunhua.domain.requestRo.MerchantInfoRo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantMapper extends BaseMapper<ChkMerchantInfo> {


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
            "        -- 停用,删除,待施工油站不显示\n" +
            "        ORDER BY\n" +
            "            distance" +
            "</script>")
    List<ChkMerchantInfo> getMerchantList(MerchantInfoRo merchantInfoRo);
}
