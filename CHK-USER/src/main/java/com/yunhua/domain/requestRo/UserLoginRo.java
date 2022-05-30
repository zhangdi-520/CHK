package com.yunhua.domain.requestRo;

import lombok.Data;

/**
 * @version V1.0
 * @program: CHK
 * @description: 用户登录Ro
 * @author: Mr.Zhang
 * @create: 2022-05-10 14:13
 **/
@Data
public class UserLoginRo {

    private String mobile;

    private String smsCode;
}
