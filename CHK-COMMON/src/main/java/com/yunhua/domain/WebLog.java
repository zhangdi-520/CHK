package com.yunhua.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @version V1.0
 * @program: CHK
 * @description: 日志记录
 * @author: Mr.Zhang
 * @create: 2022-05-11 15:12
 **/
@Data
@TableName("chk_web_log")
public class WebLog implements Serializable {


    private static final long serialVersionUID = 100312021321312L;

    private String id;

    /**
     * 操作描述
     */
    private String description;


    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 操作时间
     */
    private Long startTime;

    /**
     * 消耗时间
     */
    private Integer spendTime;

    /**
     * 根路径
     */
    private String basePath;

    /**
     * URI
     */
    private String uri;

    /**
     * URL
     */
    private String url;

    /**
     * 请求类型
     */
    private String method;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 请求参数
     */
    private String parameter;

    /**
     * 请求返回的结果
     */
    private String result;


    /**
     * 错误码字段
     */
    private Integer code;
}
