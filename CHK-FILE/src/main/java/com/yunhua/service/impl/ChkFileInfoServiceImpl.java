package com.yunhua.service.impl;

import com.yunhua.dao.ChkFileInfoDao;
import com.yunhua.entity.ChkFileInfo;
import com.yunhua.entity.vo.ResponseResult;
import com.yunhua.execption.vo.ResultEnum;
import com.yunhua.mapper.ChkFileInfoMapper;
import com.yunhua.service.ChkFileInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunhua.utils.QiniuUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 魏启恒
 * @since 2022-06-17
 */
@Service
public class ChkFileInfoServiceImpl extends ServiceImpl<ChkFileInfoMapper, ChkFileInfo> implements ChkFileInfoService {

    @Autowired
    private QiniuUtils qiniuUtils;

    @Autowired
    private ChkFileInfoDao fileInfoDao;

    /**
     * 上传文件到七牛云并且将url和文件名存入文件表
     * @param file
     * @param fileInfo
     * @return
     */

    @Override
    public ResponseResult uploadFile(MultipartFile file, ChkFileInfo fileInfo) {

        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString()+"."+ StringUtils.substringAfterLast(originalFilename,".");
        fileInfo.setFileUrl(qiniuUtils.url + fileName);
        fileInfo.setFileName(fileName);
        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload){
            int insertStatus = fileInfoDao.insertFile(fileInfo);
            if (insertStatus != 0){
                return new ResponseResult(200,qiniuUtils.url + fileName);
            }else {
                return new ResponseResult(ResultEnum.ADDDATABASEFAIL.getCode(), ResultEnum.ADDDATABASEFAIL.getMsg());
            }
        }
        return new ResponseResult(ResultEnum.UPLOADFAIL.getCode(), ResultEnum.UPLOADFAIL.getMsg());
    }


    /**
     * 删除七牛云上的文件和数据库中的文件。
     * @param fileId
     * @return
     */
    @Override
    public ResponseResult deleteFile(Long fileId) {
        ChkFileInfo fileInfo = fileInfoDao.selectFileByFileId(fileId);
        if (fileInfo != null){
            int deleteStatus = fileInfoDao.deleteFileInfoByFileId(fileId);
            boolean deleteStatusInQiNiu = qiniuUtils.delete(fileInfo.getFileName());
            if (deleteStatusInQiNiu == true && deleteStatus > 0){
                return new ResponseResult(200,null);
            }
        }
        return new ResponseResult(ResultEnum.DELETEFILEFAIL.getCode(), ResultEnum.DELETEFILEFAIL.getMsg());
    }


    /**
     * 根据文件Id查询数据库中的文件信息
     * @param fileId
     * @return
     */
    @Override
    public ResponseResult getFile(Long fileId) {
        ChkFileInfo fileInfo = fileInfoDao.selectFileByFileId(fileId);
        if (fileInfo != null){
            return new ResponseResult(200,fileInfo);
        }else {
            return new ResponseResult(ResultEnum.NOTFINDINDATABASE.getCode(), ResultEnum.NOTFINDINDATABASE.getMsg());
        }
    }



    @Override
    public ResponseResult deleteBatchFiles(Long[] fileIdArr) {
        String[] fileNameArr = fileInfoDao.listFileNamesByFileIdArr(fileIdArr);
        if (fileNameArr.length > 0){
            int deleteBatchStatus = fileInfoDao.deleteBatchFileInfo(fileIdArr);
            boolean deleteBatchInQiNiu = qiniuUtils.deleteBatch(fileNameArr);
            if (deleteBatchInQiNiu == true && deleteBatchStatus > 0){
                return new ResponseResult(200,null);
            }
        }
        return new ResponseResult(ResultEnum.DELETEFILEFAIL.getCode(), ResultEnum.DELETEFILEFAIL.getMsg());
    }


}
