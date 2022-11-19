package com.example.go.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.go.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
