package com.yunhua.controller;

import com.yunhua.domain.ChkUserCarInfo;
import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.requestRo.MerchantInfoRo;
import com.yunhua.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @version V1.0
 * @program: CHK
 * @description: 车辆接口
 * @author: Mr.Zhang
 * @create: 2022-05-12 09:23
 **/
@RestController
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping("/usercar/add")
    public ResponseResult addUserCar(@RequestHeader("token") String token, @RequestBody ChkUserCarInfo chkUserCarInfo) throws Exception {
        return carService.addUserCar(token,chkUserCarInfo);
    }


    @GetMapping("/usercar/car")
    public ResponseResult getCar(@RequestHeader("token")String token) throws Exception {
        return carService.getCar(token);
    }
}
