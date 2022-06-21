package com.yunhua.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yunhua.entity.ChkFileInfo;
import com.yunhua.mapper.ChkFileInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ChkFileInfoDao {

    @Autowired
    private ChkFileInfoMapper chkFileInfoMapper;

    public int insertFile(ChkFileInfo fileInfo){
        fileInfo.setDelFlag(0);
        fileInfo.setCreateTime(new Date());
        fileInfo.setUpdateTime(new Date());
        int insertStatus = chkFileInfoMapper.insert(fileInfo);
        return insertStatus;
    }

    public ChkFileInfo selectFileByFileId(Long fileId){
        LambdaQueryWrapper<ChkFileInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChkFileInfo::getId,fileId)
                .eq(ChkFileInfo::getDelFlag,0);
        ChkFileInfo fileInfo = chkFileInfoMapper.selectOne(wrapper);
        return fileInfo;
    }

    public int deleteFileInfoByFileId(Long fileId){
        LambdaUpdateWrapper<ChkFileInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ChkFileInfo::getId,fileId)
                .eq(ChkFileInfo::getDelFlag,0)
                .set(ChkFileInfo::getDelFlag,1);
        int deleteStatus = chkFileInfoMapper.update(null, wrapper);
        return deleteStatus;
    }


    public String[] listFileNamesByFileIdArr(Long[] fileIdArr){

        String[] fileNameArr = chkFileInfoMapper.listFileNamesByFileIdArr(fileIdArr);
        return fileNameArr;
    }

    public int deleteBatchFileInfo(Long[] fileIdArr){

        LambdaUpdateWrapper<ChkFileInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(ChkFileInfo::getId,fileIdArr)
                .eq(ChkFileInfo::getDelFlag,0)
                .set(ChkFileInfo::getDelFlag,1);

        int deleteBatchStatus = chkFileInfoMapper.update(null, wrapper);
        return deleteBatchStatus;
    }


}
