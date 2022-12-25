package com.example.go.controller.manager;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.go.common.MessageNews;
import com.example.go.common.R;
import com.example.go.entity.Manager;
import com.example.go.entity.Railway;
import com.example.go.mapper.RailwayMapper;
import com.example.go.service.manager.ManagerService;
import com.example.go.service.manager.RailWayManagerService;
import com.example.go.utils.GetNowTime;
import com.example.go.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/managerRailway")
public class ManagerRailwayController {
    @Autowired
    private RailWayManagerService railWayManagerService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private RailwayMapper railwayMapper;

    @PostMapping("/add/{id}")
    public R addRailway(@PathVariable Integer id, @RequestBody Railway railway) throws ParseException {
        //得到商家
        Manager manager = managerService.isManager(id);
        boolean b = railWayManagerService.addRailway(manager.getId(), railway);
        //将机票添加到分类表
        //添加商家与分类表的类型
        if( !b ){
            return R.error(MessageNews.MESSAGE_BUSINESS_ADD_RAILWAY_FAIL);
        }
        return R.success(MessageNews.MESSAGE_BUSINESS_ADD_RAILWAY_SUCCESS);
    }

    @PostMapping("/changeRailway")
    public R changeRailway(@RequestBody Railway railway) throws ParseException {
        Date date = GetNowTime.getNowTime();
        LambdaUpdateWrapper<Railway> railwayLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        railwayLambdaUpdateWrapper.set(Railway::getStartPlace,railway.getStartPlace())
                .set(Railway::getEndPlace,railway.getEndPlace())
                .set(Railway::getStartTime,railway.getStartTime())
                .set(Railway::getEndTime,railway.getEndTime())
                .set(Railway::getNum,railway.getNum())
                .set(Railway::getPrice,railway.getPrice())
                .set(Railway::getNum,railway.getNum())
                .set(Railway::getDiscount,railway.getDiscount())
                .set(Railway::getUpdateTime,date)
                .eq(Railway::getId,railway.getId());
        int row = railwayMapper.update(null, railwayLambdaUpdateWrapper);
        if(row != 1){
            return R.error(MessageNews.MESSAGE_MANAGER_CHANGE_RAILWAY_FAIL);
        }
        return R.success(MessageNews.MESSAGE_MANAGER_CHANGE_RAILWAY_SUCCESS);
    }

    @DeleteMapping("/deleteRailway/{id}/{token}")
    public R deleteRailway(@PathVariable("id") int id,@PathVariable("token") String token){
        int bid = JwtUtils.getId(token);
        railWayManagerService.deleteRailway(id,bid);
        return R.success(MessageNews.MESSAGE_MANAGER_DELETE_RAILWAY_SUCCESS);
    }

}