package com.yunhua.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 魏启恒
 * @since 2022-05-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ChkMerchantEmployee对象", description="")
public class ChkMerchantEmployee implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long merchantId;

    private String name;

    private String cardNo;

    private String mobile;

    private String sex;

    private Integer delFlag;

    private Date createTime;

    private Date updateTime;

    private String creatorId;



}
