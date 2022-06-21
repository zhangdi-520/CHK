package com.yunhua.mapper;

import com.yunhua.entity.ChkFileInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 魏启恒
 * @since 2022-06-17
 */
@Repository
public interface ChkFileInfoMapper extends BaseMapper<ChkFileInfo> {
    public String[] listFileNamesByFileIdArr(@Param("fileIdArr") Long[] fileIdArr);
}
