package com.example.go.controller.page;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.go.mapper.UserMapper;
import com.example.go.common.R;
import com.example.go.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/selectUser")
public class PageUserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/allUsers/{current}/{limit}")
    public R<Page> allUsers(@PathVariable long current, @PathVariable long limit, @RequestParam(value= "name" ,required = false) String name){
        //构造分页构造器
        Page page = new Page(current,limit);
        System.out.println(name);
        //条件构造器
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        if ( name.isEmpty() ){
            page = userMapper.selectPage(page,null);
        }else{
            queryWrapper.like( User::getName,name );
            queryWrapper.orderByDesc(User::getCreateTime);
            //查询
            page = userMapper.selectPage(page,queryWrapper);
        }
        return R.success(page);
    }

    @GetMapping("/allUsersForChat/{current}/{limit}")
    public R<Page> allUsers(@PathVariable long current, @PathVariable long limit){
        //构造分页构造器
        Page page = new Page(current,limit);
        //条件构造器
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.orderByDesc(User::getUpTime);
        //查询
        page = userMapper.selectPage(page,queryWrapper);
        return R.success(page);
    }
}
