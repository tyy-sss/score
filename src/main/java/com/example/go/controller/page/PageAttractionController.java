package com.example.go.controller.page;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.go.common.R;
import com.example.go.entity.Attraction;
import com.example.go.entity.Manager;
import com.example.go.mapper.AttractionMapper;
import com.example.go.service.business.BusinessService;
import com.example.go.service.manager.ManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/selectAttraction")
public class PageAttractionController {

    @Autowired
    private AttractionMapper attractionMapper;
    @Autowired
    private ManagerService managerService;

    @Autowired
    private BusinessService businessService;

    @GetMapping("/allAttraction/{id}/{current}/{limit}")
    public R allBusiness(@PathVariable Integer id, @PathVariable int current, @PathVariable int limit, @RequestParam(value= "name" ,required = false) String name){
        //通过token判断是商家还是管理员
        Manager manager = managerService.isManager(id);
        //构造分页构造器
        Page page = new Page(current,limit);
        if( manager.getStatus() == 0 ){//是管理员
            LambdaQueryWrapper<Attraction> queryWrapper = new LambdaQueryWrapper<>();
            //添加过滤条件
            if ( name.isEmpty() ){
                page = attractionMapper.selectPage(page,queryWrapper);
            }else{
                queryWrapper.like( Attraction::getName , name );
                queryWrapper.orderByDesc( Attraction::getCreateTime );
                //查询
                page = attractionMapper.selectPage(page,queryWrapper);
            }
            return R.success(page);
        } else {//商家
           Page page1 = businessService.selectAllGoods(manager.getId(), manager.getStatus(),current,limit);
            return R.success(page1);
        }
    }

}
