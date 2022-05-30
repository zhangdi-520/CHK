package com.yunhua.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @version V1.0
 * @program: CHK
 * @description: 车辆信息表
 * @author: Mr.Zhang
 * @create: 2022-05-12 09:32
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("chk_user_carinfo")
public class ChkUserCarInfo {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *用户唯一标识（对应chk_user_info表的主键）
     */
    private Integer userId;

    /**
     *车牌号
     */
    private String plateNo;

    /**
     *车辆型号
     */
    private String plateType;

    /**
     *车辆里程
     */
    private Integer carMileage;

    /**
     *创建时间
     */
    private Date createTime;

    /**
     *修改时间
     */
    private Date updateTime;
}
