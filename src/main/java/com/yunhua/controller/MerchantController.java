package com.yunhua.controller;

import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.requestRo.MerchantInfoRo;
import com.yunhua.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version V1.0
 * @program: CHK
 * @description: 商铺接口
 * @author: Mr.Zhang
 * @create: 2022-05-12 09:23
 **/
@RestController
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @PostMapping("/usercar/list")
    public ResponseResult carList(@RequestBody MerchantInfoRo merchantInfoRo){
        return merchantService.carList(merchantInfoRo);
    }
}
