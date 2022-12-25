package com.example.go.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.go.entity.Hotel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HotelMapper extends BaseMapper<Hotel> {
    //获得一个商家的所有酒店产品
    int allHotelForBusiness(int businessId);
    //获得一个商家的分页的所有酒店产品
    List<Hotel> pageHotelForBusiness(int businessId,int current,int size);

    //获得推荐的前几名的酒店
    List<Hotel> getAdviceHotel(int limit);

    //获得一个酒店的具体消息，包括酒店的名和酒店的位置
    Hotel getHotelNews(int id);
}
