package com.example.go.controller.manager;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.go.mapper.ManagerMapper;
import com.example.go.common.MessageNews;
import com.example.go.common.R;
import com.example.go.entity.Manager;
import com.example.go.service.manager.ManagerService;
import com.example.go.utils.GetNowTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * 管理员管理商家
 */
@Slf4j
@RestController
@RequestMapping("/managerBusiness")
public class ManagerBusinessController {
    @Autowired
    private ManagerMapper managerMapper;

    @Autowired
    private ManagerService managerService;


    @PostMapping("/add")
    public R addUser(@RequestBody Manager manager) throws ParseException {
        System.out.println(manager);
        //条件查询email 用户
        Manager manager1 = managerService.emailIsExists(manager.getEmail());
        //用户不存在
        if (manager1 != null) {
            return R.error(MessageNews.MESSAGE_MANAGER_REGISTER_FAIL_EMAIL_EXIST);
        }
        //设置默认密码 123456
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        manager.setPassword(password);
        //设置创建时间
        manager.setCreateTime(GetNowTime.getNowTime());
        manager.setDisable(1);
        //注册账号
        managerMapper.insert(manager);
        return R.success(MessageNews.MESSAGE_MANAGER_ADD_BUSINESS_SUCCESS);
    }

    @PostMapping("/stop")
    public R changeUser(@RequestBody Manager manager){
        System.out.println(manager);
        int disable = 0;
        if( manager.getDisable() == 0){
            disable = 1;
        }
        //条件查询email
        Manager manager1 = managerService.emailIsExists(manager.getEmail());
        LambdaUpdateWrapper<Manager> updateWrapper = new LambdaUpdateWrapper<Manager>()
                .set(Manager::getDisable,disable)
                .eq(Manager::getId,manager1.getId());
        int rows = managerMapper.update(null,updateWrapper);
        if(rows != 1){
            return R.error(MessageNews.MESSAGE_MANAGER_CHANGE_BUSINESS_FAIL);
        }
        return R.success(MessageNews.MESSAGE_MANAGER_CHANGE_BUSINESS_SUCCESS);
    }

    @DeleteMapping("/delete/{email}")
    public R deleteUser(@PathVariable("email") String email){
        //条件查询email 用户
        Manager manager1 = managerService.emailIsExists(email);
        int i = managerMapper.deleteById(manager1.getId());
        if( i == 0 ){
            return R.error(MessageNews.MESSAGE_MANAGER_DELETE_BUSINESS_FAIL);
        }
        return R.success(MessageNews.MESSAGE_MANAGER_DELETE_BUSINESS_SUCCESS);
    }

}
