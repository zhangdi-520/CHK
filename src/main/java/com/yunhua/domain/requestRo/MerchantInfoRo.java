package com.yunhua.domain.requestRo;


import lombok.Data;

@Data
public class MerchantInfoRo {

    //经度
    private Double longitude;

    //纬度
    private Double latitude;

    //距离
    private String radius;

    //筛选类型（2评分筛选）
    private Integer type;
}
