package com.yunhua.service;

import com.yunhua.entity.ChkHkInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunhua.entity.vo.ResponseResult;
import com.yunhua.golbalexception.vo.ResultEnum;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 魏启恒
 * @since 2022-06-06
 */
public interface ChkHkInfoService extends IService<ChkHkInfo> {
    List<ChkHkInfo> listAllHkInfo();
    ChkHkInfo getHkInfoByHkId(Long hkId);
    ResponseResult addHkInfo(ChkHkInfo hkInfo);
    ResponseResult deleteHkInfoByHkId(Long hkId);
    ResponseResult updateHkInfoByHkId(Long hkId,ChkHkInfo hkInfo);
}
