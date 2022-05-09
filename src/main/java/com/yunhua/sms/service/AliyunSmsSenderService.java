package com.yunhua.sms.service;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.yunhua.domain.ResponseResult;

/**
 * @Author:
 * @Description: 阿里云短信发送service
 * @Date: 17:31 2022/5/9
 * @Version: V1.0
 */
public interface AliyunSmsSenderService {

    /**
     * @param phoneNumbers:      手机号
     * @param templateParamJson: 模板参数json {"sellerName":"123456","orderSn":"123456"}
     * @param templateCode:      阿里云短信模板code
     * @Author:
     * @Description: 对接阿里云短信服务实现短信发送
     * 发送验证码类的短信时，每个号码每分钟最多发送一次，每个小时最多发送5次。其它类短信频控请参考阿里云
     * @Date: 2022/5/9 16:35
     * @Version: V1.0
     */
    SendSmsResponse sendSms(String phoneNumbers, String templateParamJson, String templateCode);

    /**
     * @param bizId:       短信对象的对应的bizId
     * @param phoneNumber: 手机号
     * @param pageSize:    分页大小
     * @param currentPage: 当前页码
     * @Author:
     * @Description: 查询发送短信的内容
     * @Date: 2022/5/9 16:52
     * @Version: V1.0
     */
    QuerySendDetailsResponse querySendDetails(String bizId, String phoneNumber, Long pageSize, Long currentPage);

    /**
     * @Author:
     * @Description: 发送短信相关逻辑
     * @Date: 2022/5/9 16:52
     * @Version: V1.0
     */
    ResponseResult sms(String mobile);
}
