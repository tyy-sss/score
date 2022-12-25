package com.example.go.controller.manager;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.go.common.MessageNews;
import com.example.go.common.R;
import com.example.go.entity.Hotel;
import com.example.go.entity.Manager;
import com.example.go.mapper.HotelMapper;
import com.example.go.service.manager.HotelManagerService;
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
@RequestMapping("/managerHotel")
public class ManagerHotelController {

    @Autowired
    private HotelManagerService hotelManagerService;

    @Autowired
    private ManagerService managerService;
    @Autowired
    private HotelMapper hotelMapper;


    @PostMapping("/add/{id}")
    public R addTicket(@PathVariable("id") Integer id, @RequestBody Hotel hotel) throws ParseException {
        //得到商家
        Manager manager = managerService.isManager(id);
        boolean b = hotelManagerService.addHotel(manager.getId(), hotel);
        //将机票添加到分类表
        //添加商家与分类表的类型
        if( !b ){
            return R.error(MessageNews.MESSAGE_BUSINESS_ADD_HOTEL_FAIL);
        }
        return R.success(MessageNews.MESSAGE_BUSINESS_ADD_HOTEL_SUCCESS);
    }

    @PostMapping("/changeHotel")
    public R changeTicket(@RequestBody Hotel hotel) throws ParseException {
        Date date = GetNowTime.getNowTime();
        LambdaUpdateWrapper<Hotel> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Hotel::getName,hotel.getName())
                .set(Hotel::getImg,hotel.getImg())
                .set(Hotel::getDescription,hotel.getDescription())
                .set(Hotel::getPrice,hotel.getPrice())
                .set(Hotel::getNum,hotel.getNum())
                .set(Hotel::getDiscount,hotel.getDiscount())
                .set(Hotel::getUpdateTime,date)
                .eq(Hotel::getId,hotel.getId());
        int row = hotelMapper.update(null, updateWrapper);
        if(row != 1){
            return R.error(MessageNews.MESSAGE_MANAGER_CHANGE_TICKET_FAIL);
        }
        return R.success(MessageNews.MESSAGE_MANAGER_CHANGE_TICKET_SUCCESS);
    }

    @DeleteMapping("/deleteHotel/{id}/{token}")
    public R deleteTicket(@PathVariable("id") int id,@PathVariable("token") String token){
        Integer bid = JwtUtils.getId(token);
        hotelManagerService.deleteHotel(id,bid);
        return R.success(MessageNews.MESSAGE_MANAGER_DELETE_HOTEL_SUCCESS);
    }

}
