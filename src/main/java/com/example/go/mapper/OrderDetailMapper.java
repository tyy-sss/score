package com.example.go.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.go.entity.Order;
import com.example.go.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper  extends BaseMapper<OrderDetail> {

    //通过订单的id得到订单的具体消息
    OrderDetail getOrderNews(int id);
    //获得管理员分页查询的订单的消息
    List<OrderDetail> checkPageOrderByManager(int current, int size, String status);
    //获得分页查询的总数
    int  allOrderByManager(String status);

    List<OrderDetail> checkPageOrderByManagerAndCode(int current, int size, String status, String code);

    int  allOrderByManagerAndCode(String status,String code);

    List<OrderDetail> checkPageOrderByBusiness(int bid,int current, int size, String status);

    int allOrderByBusiness(int bid, String status);
    List<OrderDetail> checkPageOrderByBusinessAndCode(int bid, int current, int size, String status, String code);

    int allOrderByBusinessAndCode(int bid, String status, String code);

    List<OrderDetail> getAttractionOrderNews(int id);
}
