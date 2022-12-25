package com.example.go.controller.page;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.go.common.R;
import com.example.go.entity.Manager;
import com.example.go.entity.Railway;
import com.example.go.mapper.RailwayMapper;
import com.example.go.service.business.BusinessService;
import com.example.go.service.manager.ManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/selectRailway")
public class PageRailwayController {
    @Autowired
    private RailwayMapper railwayMapper;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private BusinessService businessService;

    @GetMapping("/allRailway/{id}/{current}/{limit}")
    public R allBusiness(@PathVariable Integer id, @PathVariable int current, @PathVariable int limit, @RequestParam(value= "name" ,required = false) String name){
        //通过token判断是商家还是管理员
        Manager manager = managerService.isManager(id);
        //构造分页构造器
        Page page = new Page(current,limit);
        if( manager.getStatus() == 0 ){//是管理员
            LambdaQueryWrapper<Railway> queryWrapper = new LambdaQueryWrapper<>();
            //添加过滤条件
            if ( name.isEmpty() ){
                page = railwayMapper.selectPage(page,queryWrapper);
            }else{
                queryWrapper.like( Railway::getStartPlace , name );
                queryWrapper.orderByDesc(Railway::getCreateTime);
                //查询
                page = railwayMapper.selectPage(page,queryWrapper);
            }
            return R.success(page);
        } else {//商家
            Page page1 = businessService.selectAllGoods(manager.getId(), manager.getStatus(),current,limit);
            return R.success(page1);
        }
    }
}
