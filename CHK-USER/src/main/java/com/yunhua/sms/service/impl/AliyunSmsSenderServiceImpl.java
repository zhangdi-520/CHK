package com.yunhua.sms.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.yunhua.constant.RedisConstant;
import com.yunhua.domain.ResponseResult;
import com.yunhua.sms.domian.AliyunSMSConfig;
import com.yunhua.sms.domian.Sms;
import com.yunhua.sms.domian.SmsQuery;
import com.yunhua.sms.service.AliyunSmsSenderService;
import com.yunhua.utils.CommonUtil;
import com.yunhua.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author:
 * @Description: 发送短信封装服务实现类
 * @Date: 2022/5/9 15:36
 * @Version: V1.0
 */
@Slf4j
@Service
public class AliyunSmsSenderServiceImpl implements AliyunSmsSenderService {

    @Resource
    private AliyunSMSConfig smsConfig;

    @Autowired
    private RedisCache redisCache;

    @Override
    public SendSmsResponse sendSms(String phoneNumbers, String templateParamJson, String templateCode) {

        // 封装短信发送对象
        Sms sms = new Sms();
        sms.setPhoneNumbers(phoneNumbers);
        sms.setTemplateParam(templateParamJson);
        sms.setTemplateCode(templateCode);

        // 获取短信发送服务机
        IAcsClient acsClient = getClient();

        //获取短信请求
        SendSmsRequest request = getSmsRequest(sms);
        SendSmsResponse sendSmsResponse = new SendSmsResponse();

        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            log.error("发送短信发生错误。错误代码是 [{}]，错误消息是 [{}]，错误请求ID是 [{}]，错误Msg是 [{}]，错误类型是 [{}]",
                    e.getErrCode(),
                    e.getMessage(),
                    e.getRequestId(),
                    e.getErrMsg(),
                    e.getErrorType());
        }
        return sendSmsResponse;
    }

    @Override
    public QuerySendDetailsResponse querySendDetails(String bizId, String phoneNumber, Long pageSize, Long currentPage) {

        // 查询实体封装
        SmsQuery smsQuery = new SmsQuery();
        smsQuery.setBizId(bizId);
        smsQuery.setPhoneNumber(phoneNumber);
        smsQuery.setCurrentPage(currentPage);
        smsQuery.setPageSize(pageSize);
        smsQuery.setSendDate(new Date());

        // 获取短信发送服务机
        IAcsClient acsClient = getClient();
        QuerySendDetailsRequest request = getSmsQueryRequest(smsQuery);
        QuerySendDetailsResponse querySendDetailsResponse = null;
        try {
            querySendDetailsResponse = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            log.error("查询发送短信发生错误。错误代码是 [{}]，错误消息是 [{}]，错误请求ID是 [{}]，错误Msg是 [{}]，错误类型是 [{}]",
                    e.getErrCode(),
                    e.getMessage(),
                    e.getRequestId(),
                    e.getErrMsg(),
                    e.getErrorType());
        }
        return querySendDetailsResponse;
    }

    @Override
    public ResponseResult sms(String mobile) {
        if (!StringUtils.hasText(mobile)){
            return new ResponseResult(102,"参数不符合规范");
        }
        //进行校验，防止用户暴力刷接口
        String smsCode;
        smsCode = redisCache.getCacheObject(RedisConstant.SMS + mobile);
        if (StringUtils.hasText(smsCode)){
            return new ResponseResult(103,"短信接口繁忙，请稍后重试");
        }
        smsCode = CommonUtil.randomCode();
        Map<String, String> map = new HashMap<>();
        map.put("code", smsCode);
        SendSmsResponse sendSmsResponse = sendSms(mobile,
                JSON.toJSONString(map),
                smsConfig.getTemplateCode());
        if (!"200".equals(sendSmsResponse.getCode())){
            return new ResponseResult(101,"发送短信失败");
        }
        log.info("=======================>向手机号{}发送验证{}成功",mobile,smsCode);
        //发送短信成功向redis中缓存一份数据
        redisCache.setCacheObject(RedisConstant.SMS+ mobile,smsCode,RedisConstant.SMSEXPIRE, TimeUnit.SECONDS);

        return new ResponseResult(200,"发送短信成功");
    }



    /**
     * @param smsQuery:
     * @Author:
     * @Description: 封装查询阿里云短信请求对象
     * @Date: 2022/5/9 16:51
     * @Version: V1.0
     */
    private QuerySendDetailsRequest getSmsQueryRequest(SmsQuery smsQuery) {
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        request.setPhoneNumber(smsQuery.getPhoneNumber());
        request.setBizId(smsQuery.getBizId());
        SimpleDateFormat ft = new SimpleDateFormat(smsConfig.getDateFormat());
        request.setSendDate(ft.format(smsQuery.getSendDate()));
        request.setPageSize(smsQuery.getPageSize());
        request.setCurrentPage(smsQuery.getCurrentPage());
        return request;
    }


    /**
     * @param sms: 短信发送实体
     * @Author:
     * @Description: 获取短信请求
     * @Date: 2022/5/9 16:40
     * @Version: V1.0
     */
    private SendSmsRequest getSmsRequest(Sms sms) {
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(sms.getPhoneNumbers());
        request.setSignName(smsConfig.getSignName());
        request.setTemplateCode(sms.getTemplateCode());
        request.setTemplateParam(sms.getTemplateParam());
        request.setOutId(sms.getOutId());
        return request;
    }

    /**
     * @Author:
     * @Description: 获取短信发送服务机
     * @Date: 2022/5/9 16:38
     * @Version: V1.0
     */
    private IAcsClient getClient() {

        IClientProfile profile = DefaultProfile.getProfile(smsConfig.getRegionId(),
                smsConfig.getAccessKeyId(),
                smsConfig.getAccessKeySecret());

        try {
            DefaultProfile.addEndpoint(smsConfig.getEndpointName(),
                    smsConfig.getRegionId(),
                    smsConfig.getProduct(),
                    smsConfig.getDomain());
        } catch (ClientException e) {
            log.error("获取短信发送服务机发生错误。错误代码是 [{}]，错误消息是 [{}]，错误请求ID是 [{}]，错误Msg是 [{}]，错误类型是 [{}]",
                    e.getErrCode(),
                    e.getMessage(),
                    e.getRequestId(),
                    e.getErrMsg(),
                    e.getErrorType());
        }
        return new DefaultAcsClient(profile);
    }


}

