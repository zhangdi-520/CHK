package com.yunhua.service;

import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.WebLog;

import java.io.IOException;
import java.util.List;

public interface WebLogService {

    public int insertWebLog(WebLog webLog);
    public void insertWebLogIntoES(WebLog webLog,String index) throws IOException;
    public ResponseResult listAllWebLogPage(int pageNum , int pageSize,String index) throws IOException;
    public ResponseResult listWebLogByUserId(long userId,String index) throws IOException;
    public ResponseResult listWebLogPageByUserId(int pageNum , int pageSize,long userId,String index) throws IOException;
    public ResponseResult listWebLogPageByCode(int pageNum, int pageSize ,int code,String index) throws IOException;
    public ResponseResult listWebLogPageByUserIdAndCode(int pageNum, int pageSize ,long userId,int code,String index) throws IOException;
}
