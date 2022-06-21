package com.yunhua.service;

import com.yunhua.entity.ChkFileInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunhua.entity.vo.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 魏启恒
 * @since 2022-06-17
 */
public interface ChkFileInfoService extends IService<ChkFileInfo> {
    public ResponseResult uploadFile(MultipartFile file, ChkFileInfo fileInfo);
    public ResponseResult deleteFile(Long fileId);
    public ResponseResult getFile(Long fileId);
    public ResponseResult deleteBatchFiles(Long[] fileIdArr);
}
