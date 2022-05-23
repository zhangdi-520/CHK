package com.yunhua.service.impl;


import com.yunhua.config.BasicConfig;
import com.yunhua.dao.MerchantDao;
import com.yunhua.domain.ChkMerchantInfo;
import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.requestRo.MerchantInfoRo;
import com.yunhua.golbalexception.exception.BusinessException;
import com.yunhua.golbalexception.vo.ResultEnum;
import com.yunhua.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private BasicConfig basicConfig;

    @Autowired
    private MerchantDao merchantDao;

    @Override
    public ResponseResult carList(MerchantInfoRo merchantInfoRo) {
        if (merchantInfoRo.getLatitude() == null || merchantInfoRo.getLongitude() == null){
            throw new BusinessException(ResultEnum.PARAMCHECHFAIL);
        }
        log.info("判断距离是否为空，为商家设置默认距离");
        if (merchantInfoRo.getRadius() == null){
            merchantInfoRo.setRadius(basicConfig.getChkMerchantDistant());
        }
        List<ChkMerchantInfo> merchantList = null;
        merchantList = merchantDao.getMerchantList(merchantInfoRo);
        if (merchantInfoRo.getType() == 2){
            log.info("按照评分筛选商家");
            merchantList = merchantList.stream().sorted(Comparator.comparing(ChkMerchantInfo::getScore)).collect(Collectors.toList());
        }
        return new ResponseResult(200,merchantList);
    }

}
