package com.example.go.service.business;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.go.entity.*;
import com.example.go.mapper.AttractionMapper;
import com.example.go.mapper.BusinessGoodsMapper;
import com.example.go.mapper.HotelMapper;
import com.example.go.mapper.RailwayMapper;
import com.example.go.mapper.TicketMapper;
import com.example.go.mapper.TrainMapper;
import com.example.go.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商家
 */
@Service
public class BusinessService {

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private HotelMapper hotelMapper;

    @Autowired
    private TrainMapper trainMapper;

    @Autowired
    private RailwayMapper railwayMapper;

    @Autowired
    private AttractionMapper attractionMapper;

    @Autowired
    private BusinessGoodsMapper businessGoodsMapper;

    //查询商家的所有产品
    public Page selectAllGoods(int id,int status,int current,int size){
        Page page;
        if (status == 1){//机票
            int pageSum = ticketMapper.allTicketForBusiness(id);
            List<Ticket> tickets = ticketMapper.pageTicketForBusiness(id,size*(current-1),size);
            page = PageUtils.setPage(pageSum,tickets,size);
            return page;
        } else if (status == 2){//酒店
            int pageSum = hotelMapper.allHotelForBusiness(id);
            List<Hotel> hotels = hotelMapper.pageHotelForBusiness(id,size*(current-1),size);
            page = PageUtils.setPage(pageSum,hotels,size);
            return page;
        } else if (status == 3 ){//景点
            int pageSum = attractionMapper.allAttractionForBusiness(id);
            List<Attraction> attractions = attractionMapper.pageAttractionForBusiness(id,size*(current-1),size);
            page = PageUtils.setPage(pageSum,attractions,size);
            return page;
        } else if (status == 4){//train 火车
            int pageSum = trainMapper.allTrainForBusiness(id);
            List<Train> trains = trainMapper.pageTrainForBusiness(id,size*(current-1),size);
            page = PageUtils.setPage(pageSum,trains,size);
            return page;
        } else if (status == 5){//railway高铁
            int pageSum = railwayMapper.allRailwayForBusiness(id);
            List<Railway> railways = railwayMapper.pageRailwayForBusiness(id,size*(current-1),size);
            page = PageUtils.setPage(pageSum,railways,size);
            return page;
        }
        return null;
    }
    //把商家表和分类表连起来
    public int addBusinessGoods(int bId,int cId){
        BusinessGoods businessGoods = new BusinessGoods();
        businessGoods.setBusinessId(bId);
        businessGoods.setGoodsId(cId);
        int insert = businessGoodsMapper.insert(businessGoods);
        return insert;
    }
}
