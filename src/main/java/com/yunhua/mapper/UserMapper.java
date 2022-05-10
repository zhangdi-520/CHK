package com.yunhua.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunhua.domain.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {

    User findUserByMobile(@Param("mobile")String mobile);

    void insertUser(User user);
}
