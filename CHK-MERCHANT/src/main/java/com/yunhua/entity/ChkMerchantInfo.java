package com.yunhua.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author 魏启恒
 * @since 2022-05-31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="ChkMerchantInfo对象", description="")
public class ChkMerchantInfo implements Serializable {

    private static final long serialVersionUID = -403567533868312L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String merchantName;

    private String pwd;

    private String merchantPictureUrl;

    private String qualificationCertificate;

    private String businessLicense;

    private String license;

    private String environmentUrl;

    private String bank;

    private String bankAmount;

    private String bankCardNo;

    private String serviceId;

    private String address;

    private String mobile;

    private String name;

    private String cardNo;

    private String workTime;

    private Integer score;

    private Integer delFlag;

    private Date createTime;

    private Date updateTime;

    private String creatorId;
    private String latitude;

    private String longitude;

}
