package com.example.go.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.go.entity.Attraction;
import com.example.go.entity.Ticket;
import com.example.go.entity.Train;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface TicketMapper extends BaseMapper<Ticket> {
    //获得一个商家的所有机票产品
    int allTicketForBusiness(int businessId);

    //获得一个商家的分页的所有机票产品
    List<Ticket> pageTicketForBusiness(int businessId,int current,int size);

    //得到推荐机票
    List<Ticket> getAdviceTicket(int limit);

    //得到机票的全部消息
    Ticket getTicketNews(int id);
}
