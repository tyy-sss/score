package com.example.go.controller.page;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.go.mapper.ManagerMapper;
import com.example.go.common.R;
import com.example.go.entity.Manager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/selectBusiness")
public class PageBusinessController {
    @Autowired
    private ManagerMapper managerMapper;

    @GetMapping("/allBusiness/{current}/{limit}")
    public R<Page> allBusiness(@PathVariable long current, @PathVariable long limit, @RequestParam(value= "name" ,required = false) String name){
        //构造分页构造器
        Page page = new Page(current,limit);
        System.out.println(name);
        //条件构造器
        LambdaQueryWrapper<Manager> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Manager::getStatus,0);
        //添加过滤条件
        if ( name.isEmpty() ){
            page = managerMapper.selectPage(page,queryWrapper);
        }else{
            queryWrapper.like( Manager::getUsername,name );
            queryWrapper.orderByDesc(Manager::getCreateTime);
            //查询
            page = managerMapper.selectPage(page,queryWrapper);
        }
        return R.success(page);
    }
}
