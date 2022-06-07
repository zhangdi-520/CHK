package com.yunhua.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunhua.domain.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @version V1.0
 * @program: CHK
 * @description: 权限校验接口
 * @author: Mr.Zhang
 * @create: 2022-05-09 14:27
 **/
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long userId);
}
