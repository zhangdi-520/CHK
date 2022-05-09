package com.yunhua.utils;

import java.util.Random;

/**
 * @version V1.0
 * @program: CHK
 * @description: 工具类
 * @author: Mr.Zhang
 * @create: 2022-05-09 17:35
 **/
public class CommonUtil {

    public static String randomCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
}
