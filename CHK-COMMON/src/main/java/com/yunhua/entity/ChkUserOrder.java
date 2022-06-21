package com.yunhua.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 魏启恒
 * @since 2022-06-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ChkUserOrder对象", description="")
public class ChkUserOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long carId;

    private String orderNo;

    private Long serviceId;

    private Long merchantId;

    private Long merchantEmployeeId;

    private String message;

    private String pictureUrl;

    private String carInfoPictureUrl;

    private String serviceUrl;

    private Integer getCarType;

    private String getCarAddress;

    private Date getCarTime;

    private String backCarAddress;

    private Date backCarTime;

    private BigDecimal amount;

    private Integer orderState;

    private Long otherServiceId;

    private Date createTime;

    private Date updateTime;

    private String creatorId;


}
