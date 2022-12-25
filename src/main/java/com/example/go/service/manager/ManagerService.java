package com.example.go.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.go.mapper.ManagerMapper;
import com.example.go.entity.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {

    @Autowired
    private ManagerMapper managerMapper;

    //判断这个邮箱是否已经注册
    public Manager emailIsExists(String email){
        LambdaQueryWrapper<Manager> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Manager::getEmail,email);
        Manager manager = managerMapper.selectOne(queryWrapper);
        return manager;
    }

    //判断一下传过来是否是管理员
    public Manager isManager(Integer id){
        //判断是否是管理员
        Manager manager = managerMapper.selectById(id);
        return manager;
    }

    //根据username找user
    public Manager getManagerByUsername(String username){
        return managerMapper.getManagerByUsername(username);
    }
}
