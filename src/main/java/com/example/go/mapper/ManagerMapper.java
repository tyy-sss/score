package com.example.go.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.go.entity.Manager;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ManagerMapper extends BaseMapper<Manager> {

    //根据username来找manager
    Manager getManagerByUsername(String username);
}
