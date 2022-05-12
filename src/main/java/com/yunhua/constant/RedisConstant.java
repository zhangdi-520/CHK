package com.yunhua.constant;

/**
 * @version V1.0
 * @program: CHK
 * @description: 缓存常量类
 * @author: Mr.Zhang
 * @create: 2022-05-10 14:04
 **/
public class RedisConstant {

    /**
     * 用户信息
     */
    public static final String USERINFO = "USER:";

    /**
     * 用户信息过期时间
     */
    public static final Integer USERINFOEXOIRE = 7;

    /**
     * 短信验证信息
     */
    public static final String SMS = "SMS:";

    /**
     * 短信验证信息
     */
    public static final Integer SMSEXPIRE = 120;

    /**
     * JWT缓存信息
     */
    public static final String LOGININFO = "login:";

    /**
     * jwt缓存过期时间
     */
    public static final Integer LOFININFOEXPIRE = 3600;

    /**
     * 车辆信息缓存
     */
    public static final String CARINFO = "CAR:INFO:";

    /**
     * 车辆信息缓存过期时间（天）
     */
    public static final Integer CARINFOEXPIRE = 5;

}
