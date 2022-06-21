package com.yunhua.service.impl;

import com.yunhua.dao.WebLogDao;
import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.WebLog;
import com.yunhua.service.WebLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class WebLogServiceImpl implements WebLogService {

    @Autowired
    private WebLogDao webLogDao;


    /**
     * 插入日志信息到数据库（已弃用）
     * @param webLog
     * @return
     */

    @Override
    public int insertWebLog(WebLog webLog) {
        int insertStatus = webLogDao.insertWebLog(webLog);
        return insertStatus;
    }


    /**
     * 插入日志信息到ES中
     * @param webLog
     * @param index
     * @throws IOException
     */

    @Override
    public void insertWebLogIntoES(WebLog webLog , String index) throws IOException {
        webLogDao.insertWebLogIntoES(webLog,index);
    }

    /**
     * 查看所有的日志信息
     * @param pageNum
     * @param pageSize
     * @param index
     * @return
     * @throws IOException
     */
    @Override
    public ResponseResult listAllWebLogPage(int pageNum, int pageSize,String index) throws IOException {
        List<WebLog> webLogList = webLogDao.listAllWebLogPage(pageNum, pageSize,index);
        return new ResponseResult<>(200,webLogList);
    }

    /**
     * 根据用户Id查询日志信息
     * @param userId
     * @param index
     * @return
     * @throws IOException
     */
    @Override
    public ResponseResult listWebLogByUserId(long userId,String index) throws IOException {
        List<WebLog> webLogList = webLogDao.listWebLogByUserId(userId,index);
        return new ResponseResult<>(200,webLogList);
    }


    /**
     * 根据用户Id分页查询所有的日志信息
     * @param pageNum
     * @param pageSize
     * @param userId
     * @param index
     * @return
     * @throws IOException
     */

    @Override
    public ResponseResult listWebLogPageByUserId(int pageNum, int pageSize, long userId, String index) throws IOException {
        List<WebLog> webLogList = webLogDao.listWebLogPageByUserId(pageNum, pageSize, userId, index);
        return new ResponseResult(200,webLogList);
    }


    /**
     * 根据状态码分页查询日志信息
     * @param pageNum
     * @param pageSize
     * @param code
     * @param index
     * @return
     * @throws IOException
     */
    @Override
    public ResponseResult listWebLogPageByCode(int pageNum, int pageSize, int code,String index) throws IOException {
        List<WebLog> webLogList = webLogDao.listWebLogPageByCode(pageNum, pageSize, code, index);
        return new ResponseResult(200,webLogList);
    }


    /**
     * 根据用户Id和状态码去查询日志信息
     * @param pageNum
     * @param pageSize
     * @param userId
     * @param code
     * @param index
     * @return
     * @throws IOException
     */
    @Override
    public ResponseResult listWebLogPageByUserIdAndCode(int pageNum, int pageSize, long userId, int code, String index) throws IOException {
        List<WebLog> webLogList = webLogDao.listWebLogPageByUserIdAndCode(pageNum, pageSize, userId, code, index);
        return new ResponseResult(200,webLogList);
    }

}
