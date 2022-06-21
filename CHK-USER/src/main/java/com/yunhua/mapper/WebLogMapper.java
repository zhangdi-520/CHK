package com.yunhua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunhua.domain.WebLog;
import org.springframework.stereotype.Repository;

/**
 * @version V1.0
 * @program: CHK
 * @description: 日志DB操作类
 * @author: Mr.Zhang
 * @create: 2022-05-11 16:34
 **/
@Repository
public interface WebLogMapper extends BaseMapper<WebLog> {

    void insertWebLog(WebLog webLog);
}
