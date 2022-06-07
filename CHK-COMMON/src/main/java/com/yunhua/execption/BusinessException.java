package com.yunhua.execption;


import com.yunhua.execption.vo.ResultEnum;

/**
 * @version V1.0
 * @program: CHK
 * @description: 自定义异常
 * @author: Mr.Zhang
 * @create: 2022-05-10 16:16
 **/
public class BusinessException extends RuntimeException {

    private Integer code;

    public BusinessException(ResultEnum resultEnum){
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();


    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
