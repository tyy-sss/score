package com.example.go.controller.manager;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.go.common.MessageNews;
import com.example.go.common.R;
import com.example.go.entity.Attraction;
import com.example.go.entity.Manager;
import com.example.go.mapper.AttractionMapper;
import com.example.go.service.manager.AttractionManagerService;
import com.example.go.service.manager.ManagerService;
import com.example.go.utils.GetNowTime;
import com.example.go.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/managerAttraction")
public class ManagerAttractionController {

    @Autowired
    private AttractionManagerService attractionManagerService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private AttractionMapper attractionMapper;

    @PostMapping("/add/{id}")
    public R addAttraction(@PathVariable Integer id, @RequestBody Attraction attraction) throws ParseException {
        //得到商家
        Manager manager = managerService.isManager(id);
        boolean b = attractionManagerService.addAttraction(manager.getId(),attraction);
        //将机票添加到分类表
        //添加商家与分类表的类型
        if( !b ){
            return R.error(MessageNews.MESSAGE_BUSINESS_ADD_ATTRACTION_FAIL);
        }
        return R.success(MessageNews.MESSAGE_BUSINESS_ADD_ATTRACTION_SUCCESS);
    }

    @PostMapping("/changeAttraction")
    public R changeAttraction(@RequestBody Attraction attraction) throws ParseException {
        Date date = GetNowTime.getNowTime();
        LambdaUpdateWrapper<Attraction> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Attraction::getName,attraction.getName())
                .set(Attraction::getImg,attraction.getImg())
                .set(Attraction::getDescription,attraction.getDescription())
                .set(Attraction::getPrice,attraction.getPrice())
                .set(Attraction::getNum,attraction.getNum())
                .set(Attraction::getDiscount,attraction.getDiscount())
                .set(Attraction::getUpdateTime,date)
                .eq(Attraction::getId,attraction.getId());
        int row = attractionMapper.update(null, updateWrapper);
        if(row != 1){
            return R.error(MessageNews.MESSAGE_MANAGER_CHANGE_ATTRACTION_FAIL);
        }
        return R.success(MessageNews.MESSAGE_MANAGER_CHANGE_ATTRACTION_SUCCESS);
    }

    @DeleteMapping("/deleteAttraction/{id}/{token}")
    public R deleteAttraction(@PathVariable("id") int id,@PathVariable("token") String token){
        int bid = JwtUtils.getId(token);
        attractionManagerService.deleteAttraction(id,bid);
        return R.success(MessageNews.MESSAGE_MANAGER_DELETE_ATTRACTION_SUCCESS);
    }
}
