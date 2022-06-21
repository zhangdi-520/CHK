package com.yunhua.dao;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunhua.domain.ResponseResult;
import com.yunhua.domain.User;
import com.yunhua.mapper.UserMapper;
import com.yunhua.utils.HttpClientRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserDao {

    @Autowired
    private UserMapper userMapper;

    public User selectUserByMobileInDataCenter(User user){
        String responseJSON = HttpClientRequestUtil.doGet("http://localhost:10089/chk-user/get/13815688216", "UTF-8");
        ResponseResult responseResult = JSON.parseObject(responseJSON, ResponseResult.class);
        Object data = responseResult.getData();
        ObjectMapper objectMapper = new ObjectMapper();
        User userSelected = objectMapper.convertValue(data, User.class);
        return userSelected;
    }

    public int addUser(User user){
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        int insertStatus = userMapper.insert(user);
        return insertStatus;
    }

}
