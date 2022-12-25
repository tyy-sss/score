package com.example.go.controller.hotel;

import com.example.go.common.R;
import com.example.go.entity.Hotel;
import com.example.go.service.hotel.HotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 酒店搜索
 */
@Slf4j
@RestController
@RequestMapping("/hotel")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    //得到推荐酒店
    @GetMapping("/adviceHotel/{num}")
    public R getHotel(@PathVariable("num") int num){
//        System.out.println(num);
        List<Hotel> adviceHotel = hotelService.getAdviceHotel(num);
        System.out.println(adviceHotel);
        return R.success(adviceHotel);
    }

    //得到酒店的具体消息
    @GetMapping("/hotelNews/{id}")
    public R getHotelNews(@PathVariable("id") int id){
        System.out.println(id);
        Hotel hotelNews = hotelService.getHotelNews(id);
        System.out.println(hotelNews);
        return R.success(hotelNews);
    }


}
