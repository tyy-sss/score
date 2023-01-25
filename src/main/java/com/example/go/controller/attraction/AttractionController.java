package com.example.go.controller.attraction;

import com.example.go.common.R;
import com.example.go.entity.Attraction;
import com.example.go.entity.Hotel;
import com.example.go.service.attraction.AttractionService;
import com.example.go.service.hotel.HotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 酒店搜索
 */
@Slf4j
@RestController
@RequestMapping("/attraction")
public class AttractionController {

    @Autowired
    private AttractionService attractionService;

    //得到推荐热门景点
    @GetMapping("/adviceHotAttraction/{num}")
    public R getHotAttraction(@PathVariable("num") int num){
        List<Attraction> adviceHotAttraction = attractionService.getAdviceHotAttraction(num);
        System.out.println(adviceHotAttraction);
        return R.success(adviceHotAttraction);
    }

    //得到推荐便宜景点
    @GetMapping("/adviceCheapTopAttraction/{num}")
    public R getCheapTopAttraction(@PathVariable("num") int num){
        List<Attraction> adviceCheapAttraction = attractionService.getAdviceCheapAttraction(0,num);
        System.out.println(adviceCheapAttraction);
        return R.success(adviceCheapAttraction);
    }

    //得到推荐便宜景点
    @GetMapping("/adviceCheapAttraction/{begin}/{num}")
    public R getCheapAttraction(@PathVariable("begin") int begin,@PathVariable("num") int num){
        List<Attraction> adviceCheapAttraction = attractionService.getAdviceCheapAttraction(begin,num);
        System.out.println(adviceCheapAttraction);
        return R.success(adviceCheapAttraction);
    }

    //得到景点的具体消息
    @GetMapping("/attractionNews/{id}")
    public R getHotelNews(@PathVariable("id") int id){
        System.out.println(id);
        Attraction attractionNews = attractionService.getAttractionNews(id);
        System.out.println(attractionNews);
        return R.success(attractionNews);
    }

}
