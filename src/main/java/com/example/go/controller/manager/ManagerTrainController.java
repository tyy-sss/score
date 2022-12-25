package com.example.go.controller.manager;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.go.common.MessageNews;
import com.example.go.common.R;
import com.example.go.entity.Manager;
import com.example.go.entity.Train;
import com.example.go.mapper.TrainMapper;
import com.example.go.service.manager.ManagerService;
import com.example.go.service.manager.TrainManagerService;
import com.example.go.utils.GetNowTime;
import com.example.go.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/managerTrain")
public class ManagerTrainController {
    @Autowired
    private TrainManagerService trainManagerService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private TrainMapper trainMapper;

    @PostMapping("/add/{id}")
    public R addTrain(@PathVariable Integer id, @RequestBody Train train) throws ParseException {
        //得到商家
        Manager manager = managerService.isManager(id);
        boolean b = trainManagerService.addTrain(manager.getId(), train);
        //将机票添加到分类表
        //添加商家与分类表的类型
        if( !b ){
            return R.error(MessageNews.MESSAGE_BUSINESS_ADD_TRAIN_FAIL);
        }
        return R.success(MessageNews.MESSAGE_BUSINESS_ADD_TRAIN_SUCCESS);
    }

    @PostMapping("/changeTrain")
    public R changeTrain(@RequestBody Train train) throws ParseException {
        Date date = GetNowTime.getNowTime();
        LambdaUpdateWrapper<Train> trainLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        trainLambdaUpdateWrapper.set(Train::getStartPlace,train.getStartPlace())
                .set(Train::getEndPlace,train.getEndPlace())
                .set(Train::getStartTime,train.getStartTime())
                .set(Train::getEndTime,train.getEndTime())
                .set(Train::getNum,train.getNum())
                .set(Train::getPrice,train.getPrice())
                .set(Train::getNum,train.getNum())
                .set(Train::getDiscount,train.getDiscount())
                .set(Train::getUpdateTime,date)
                .eq(Train::getId,train.getId());
        int row = trainMapper.update(null, trainLambdaUpdateWrapper);
        if(row != 1){
            return R.error(MessageNews.MESSAGE_MANAGER_CHANGE_TRAIN_FAIL);
        }
        return R.success(MessageNews.MESSAGE_MANAGER_CHANGE_TRAIN_SUCCESS);
    }

    @DeleteMapping("/deleteTrain/{id}/{token}")
    public R deleteTrain(@PathVariable("id") int id,@PathVariable("token") String token){
        int bid = JwtUtils.getId(token);
        trainManagerService.deleteTrain(id,bid);
        return R.success(MessageNews.MESSAGE_MANAGER_DELETE_TRAIN_SUCCESS);
    }

}
