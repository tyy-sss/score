package com.example.go.service.hotel;

import com.example.go.entity.Hotel;
import com.example.go.mapper.HotelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {
    @Autowired
    private HotelMapper hotelMapper;

    //推荐酒店
    public List<Hotel> getAdviceHotel(int limit){
        List<Hotel> adviceHotel = hotelMapper.getAdviceHotel(limit);
        return adviceHotel;
    }

    //得到酒店的具体消息
    public Hotel getHotelNews(int id){
        Hotel hotelNews = hotelMapper.getHotelNews(id);
        return hotelNews;
    }

}
