package com.example.go.controller;

import com.example.go.Mapper.ManagerMapper;
import com.example.go.entity.Manager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerMapper managerMapper;

    @GetMapping("/login")
    public Manager show(){
        System.out.println(managerMapper.selectById(1));
        return managerMapper.selectById(1);
    }

}
