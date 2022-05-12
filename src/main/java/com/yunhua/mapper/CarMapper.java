package com.yunhua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunhua.domain.ChkUserCarInfo;

/**
 * @version V1.0
 * @program: CHK
 * @description: 车辆信息ORM操作接口
 * @author: Mr.Zhang
 * @create: 2022-05-12 09:41
 **/
public interface CarMapper extends BaseMapper<ChkUserCarInfo> {

    void insertCarInfo(ChkUserCarInfo chkUserCarInfo);
}
