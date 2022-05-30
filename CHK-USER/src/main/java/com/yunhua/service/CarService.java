package com.yunhua.service;

import com.yunhua.domain.ChkUserCarInfo;
import com.yunhua.domain.ResponseResult;

/**
 * @version V1.0
 * @program: CHK
 * @description: TODO
 * @author: Mr.Zhang
 * @create: 2022-05-12 09:47
 **/
public interface CarService {

    ResponseResult addUserCar(String token, ChkUserCarInfo chkUserCarInfo) throws Exception;

    ResponseResult getCar(String token) throws Exception;

}
