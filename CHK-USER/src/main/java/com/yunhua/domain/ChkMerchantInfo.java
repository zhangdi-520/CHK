package com.yunhua.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("chk_merchant_info")
public class ChkMerchantInfo {

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *商家名称
     */
    private String merchantName;

    /**
     *密码
     */
    private String pwd;

    /**
     *门脸图图片链接
     */
    private String merchantPictureUrl;

    /**
     *资质证照
     */
    private String qulificationCertificate;

    /**
     *营业执照
     */
    private String businessLicense;

    /**
     *许可证
     */
    private String license;

    /**
     *门店环境图片
     */
    private String environmentUrl;

    /**
     *开户行
     */
    private String bank;

    /**
     *银行账户
     */
    private String bankAmount;

    /**
     *银行账号
     */
    private String bankCardNo;

    /**
     *服务类型（chk_service_type）表主键
     */
    private String serviceId;

    /**
     *商家地址
     */
    private String address;

    /**
     *手机号
     */
    private String mobile;

    /**
     *法人名称
     */
    private String name;

    /**
     *法人生份证照
     */
    private String cardNo;

    /**
     *营业时间
     */
    private String workTime;

    /**
     *评分
     */
    private Integer score;

    /**
     *启用标识位
     */
    private Integer delFlag;

    /**
     *创建时间
     */
    private Date createTime;

    /**
     *更新时间
     */
    private Date updateTime;

    /**
     *创建人id
     */
    private String creatorId;

    /**
     *距离
     */
    private String distance;
}
