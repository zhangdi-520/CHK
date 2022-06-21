package com.yunhua.controller;

import com.yunhua.constant.KafkaConstant;
import com.yunhua.domain.ResponseResult;
import com.yunhua.service.WebLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping
@Api(value = "日志分析接口",tags = "日志分析信息")
public class WebLogController {

    @Autowired
    private WebLogService webLogService;

    @GetMapping("/list/Page")
    @ApiOperation("分页查询所有的日志信息")
    public ResponseResult listAllWebLogPage(int pageNum,int pageSize) throws IOException {
        return webLogService.listAllWebLogPage(pageNum,pageSize, KafkaConstant.WEBLOG);

    }
    @GetMapping("/list/userId/{uid}")
    @ApiOperation("根据用户Id查询该用户所有的日志信息")
    public ResponseResult listWebLogByUserId(@PathVariable("uid") long userId) throws IOException {
        return webLogService.listWebLogByUserId(userId, KafkaConstant.WEBLOG);

    }


    @GetMapping("/list/page/userId/{uid}")
    @ApiOperation("根据用户Id分页查询该用户下所有的日志信息")
    public ResponseResult listWebLogPageByUserId(@PathVariable("uid") long userId,int pageNum,int pageSize) throws IOException {
        return webLogService.listWebLogPageByUserId(pageNum,pageSize,userId,KafkaConstant.WEBLOG);

    }


    @GetMapping("/list/page/code/{code}")
    @ApiOperation("根据返回码code分页查询日志信息")
    public ResponseResult listWebLogPageByCode(@PathVariable("code") int code,int pageNum,int pageSize) throws IOException {
        return webLogService.listWebLogPageByCode(pageNum,pageSize,code,KafkaConstant.WEBLOG);

    }


    @GetMapping("/list/page/code/{uid}/{code}")
    @ApiOperation("根据用户Id和状态码code分页查询所有的日志信息")
    public ResponseResult listWebLogPageByUserIdAndCode(@PathVariable("uid") long userId, @PathVariable("code") int code, int pageNum,int pageSize) throws IOException {
        return webLogService.listWebLogPageByUserIdAndCode(pageNum,pageSize,userId,code,KafkaConstant.WEBLOG);

    }

}
