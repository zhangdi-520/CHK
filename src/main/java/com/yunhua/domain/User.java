package com.yunhua.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户表(User)实体类
 *
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("chk_user_info")
public class User implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;
    
    /**
    * 主键
    */
    @TableId
    private Long id;
    /**
    * 昵称
    */
    private String nickName;
    /**
    * 密码
    */
    private String pwd;
    /**
    * 姓名
    */
    private String name;
    /**
    * 身份证号码
    */
    private String cardNo;
    /**
    * 手机号
    */
    private String mobile;
    /**
    * 性别
    */
    private Integer sex;
    /**
    * 头像图片地址
    */
    private String pictureUrl;
    /**
    * 优惠卷主键
    */
    private String couponsId;
    /**
    * 启用标志位
    */
    private Integer delFlag;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 更新时间
    */
    private Date updateTime;

}