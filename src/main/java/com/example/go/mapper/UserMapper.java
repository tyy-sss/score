package com.example.go.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.go.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    //根据username来找user
    User getUserByUsername(String username);

    //修改用户的积分
    int saveScore(int id,double score);
}
