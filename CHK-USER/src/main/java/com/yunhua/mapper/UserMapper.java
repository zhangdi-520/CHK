package com.yunhua.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunhua.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<User> {

    User findUserByMobile(@Param("mobile")String mobile);

    void insertUser(User user);

    //@SqlReadSlave
    List<User> findAll();
}
