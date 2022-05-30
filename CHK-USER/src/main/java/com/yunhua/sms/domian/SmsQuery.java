package com.yunhua.sms.domian;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @Author:
 * @Description: 短信查询实体
 * @Date: 15:51 2022/5/9
 * @Version: V1.0
 */
@Data
@ToString
public class SmsQuery {
    private String bizId;
    private String phoneNumber;
    private Date sendDate;
    private Long pageSize;
    private Long currentPage;
}
