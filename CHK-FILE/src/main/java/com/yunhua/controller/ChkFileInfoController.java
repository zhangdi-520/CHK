package com.yunhua.controller;


import com.yunhua.entity.ChkFileInfo;
import com.yunhua.entity.vo.ResponseResult;
import com.yunhua.execption.vo.ResultEnum;
import com.yunhua.service.ChkFileInfoService;
import com.yunhua.utils.QiniuUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 魏启恒
 * @since 2022-06-17
 */
@RestController
@RequestMapping
@Api(value = "文件管理系统接口",tags = "文件管理系统")
public class ChkFileInfoController {

    @Autowired
    private ChkFileInfoService fileInfoService;


    @PostMapping("/upload")
    @ApiOperation("上传文件信息")
    public ResponseResult upload(@RequestBody MultipartFile file, ChkFileInfo fileInfo){
        return fileInfoService.uploadFile(file, fileInfo);
    }


    @DeleteMapping("/delete/{fid}")
    @ApiOperation("删除文件信息")
    public ResponseResult delete(@PathVariable("fid") Long fileId){
        return fileInfoService.deleteFile(fileId);
    }

    @GetMapping("/get/{fid}")
    @ApiOperation("根据文件Id查找数据库中的文件信息")
    public ResponseResult getByFileId(@PathVariable("fid") Long fileId){
        return fileInfoService.getFile(fileId);
    }


    @DeleteMapping("delete")
    @ApiOperation("根据文件Id批量删除文件信息")
    public ResponseResult deleteBatch(Long[] fileIdArr){
        return fileInfoService.deleteBatchFiles(fileIdArr);
    }

}

