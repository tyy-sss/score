package com.example.go.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.go.entity.Hotel;
import com.example.go.entity.Manager;
import com.example.go.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface OrderMapper  extends BaseMapper<Order> {
    //获得分页查询的订单的消息
    List<Order> checkPageOrder(int uId, int current, int size);
    //获得分页查询的总数
    int allOrder(int uId);

    //获得分页查询的订单的消息
    List<Order> checkPageOrderByTime(int uId, int current, int size,Timestamp time);
    //获得分页查询的总数
    int allOrderByTime(int uId,Timestamp time);
    //获得分页查询的订单的消息
    List<Order> checkPageOrderByType(int uId, int current, int size,String type);
    //获得分页查询的总数
    int allOrderByType(int uId,String type);

    //获得分页查询的订单的消息
    List<Order> checkPageOrderByTimeAndType(int uId, int current, int size, Timestamp time,String type);
    //获得分页查询的总数
    int allOrderByTimeAndType(int uId, Timestamp time,String type);




    //获得分页查询的订单的消息
    List<Order> checkPageOrderByStatus(int uId, int current, int size,String status);
    //获得分页查询的总数
    int allOrderByStatus(int uId,String status);

    //获得分页查询的订单的消息
    List<Order> checkPageOrderByStatusAndTime(int uId, int current, int size,String status,Timestamp time);
    //获得分页查询的总数
    int allOrderByStatusAndTime(int uId,String status,Timestamp time);
    //获得分页查询的订单的消息
    List<Order> checkPageOrderByStatusAndType(int uId, int current, int size,String status,String type);
    //获得分页查询的总数
    int allOrderByStatusAndType(int uId,String status,String type);

    //获得分页查询的订单的消息
    List<Order> checkPageOrderByStatusAndTimeAndType(int uId, int current, int size,String status, Timestamp time,String type);
    //获得分页查询的总数
    int allOrderByStatusAndTimeAndType(int uId,String status, Timestamp time,String type);

    //支付订单
    int payOrder(int id,String type);
}
