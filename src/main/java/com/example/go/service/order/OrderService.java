package com.example.go.service.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.go.common.MessageNews;
import com.example.go.entity.Order;
import com.example.go.entity.OrderDetail;
import com.example.go.entity.User;
import com.example.go.mapper.OrderDetailMapper;
import com.example.go.mapper.OrderMapper;
import com.example.go.mapper.UserMapper;
import com.example.go.utils.GetNowTime;
import com.example.go.utils.JwtUtils;
import com.example.go.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private UserMapper userMapper;

    //返回生成的订单编号  规则：8位日期+6位以上自增id；
    public String getOrderCode(){
        StringBuilder sb = new StringBuilder();
        //8位日期
        String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
        sb.append(yyyyMMdd);
        //6位自增以上id
        String key = UUID.randomUUID().toString().replace("-","");
        sb.append(key);
        System.out.println(sb);
        return sb.toString();
    }

    //查看一个人所有的Order
    public Page checkAllOrder(int uId, int current, int size,String status,Timestamp time, String type){
        Page page;
        current = size*(current-1);
        if(status.equals("1")) {
            if( time==null && type.isEmpty() ){//如果time和type都没有
                int pageSum = orderMapper.allOrder(uId);
                List<Order> orders = orderMapper.checkPageOrder(uId,current,size);
                page = PageUtils.setPage(pageSum,orders,size);
                return page;
            }else if( type.isEmpty() &&time != null){//只有时间
                int pageSum = orderMapper.allOrderByTime(uId,time);
                List<Order> orders = orderMapper.checkPageOrderByTime(uId,current,size,time);
                page = PageUtils.setPage(pageSum,orders,size);
                return page;
            }else if(time == null && !type.isEmpty()){//只有类型
                int pageSum = orderMapper.allOrderByType(uId,type);
                List<Order> orders = orderMapper.checkPageOrderByType(uId,current,size,type);
                page = PageUtils.setPage(pageSum,orders,size);
                return page;
            }else {//都有
                int pageSum = orderMapper.allOrderByTimeAndType(uId, time, type);
                List<Order> orders = orderMapper.checkPageOrderByTimeAndType(uId, current, size,time, type);
                page = PageUtils.setPage(pageSum, orders, size);
                return page;
            }
        } else {
            if (status.equals("3")){
                status = "1";
            }
            if( time==null && type.isEmpty() ){//如果time和type都没有
                int pageSum = orderMapper.allOrderByStatus(uId,status);
                List<Order> orders = orderMapper.checkPageOrderByStatus(uId,current,size,status);
                page = PageUtils.setPage(pageSum,orders,size);
                return page;
            }else if( type.isEmpty() &&time != null){//只有时间
                int pageSum = orderMapper.allOrderByStatusAndTime(uId,status,time);
                List<Order> orders = orderMapper.checkPageOrderByStatusAndTime(uId,current,size,status,time);
                page = PageUtils.setPage(pageSum,orders,size);
                return page;
            }else if(time == null && !type.isEmpty()){//只有类型
                int pageSum = orderMapper.allOrderByStatusAndType(uId,status,type);
                List<Order> orders = orderMapper.checkPageOrderByStatusAndType(uId,current,size,status,type);
                page = PageUtils.setPage(pageSum,orders,size);
                return page;
            }else {//都有
                int pageSum = orderMapper.allOrderByStatusAndTimeAndType(uId,status, time, type);
                List<Order> orders = orderMapper.checkPageOrderByStatusAndTimeAndType(uId, current, size,status,time, type);
                page = PageUtils.setPage(pageSum, orders, size);
                return page;
            }
        }
    }

    //创建订单
    public int createOrder(OrderDetail orderDetail) throws ParseException {
        //得到用户的id
        Integer id = JwtUtils.getId(orderDetail.getToken());
        //创建订单
        Order order = new Order();
        order.setOrderTime(GetNowTime.getNowTime());
        order.setUserId(id);
        order.setStatus("1");
        order.setAmount(orderDetail.getTotal());
        order.setCode(getOrderCode());
        orderMapper.insert(order);
        return order.getId();
    }

    //查看一个管理员的所有订单
    public Page checkAllOrderByManager(int current,int size,String status){
        current = size*(current-1);
        Page page;
        int pageSum = orderDetailMapper.allOrderByManager(status);
        List<OrderDetail> orders = orderDetailMapper.checkPageOrderByManager(current,size,status);
        page = PageUtils.setPage(pageSum, orders, size);
        return page;
    }
    //按订单查询
    public Page checkAllOrderByManagerAndCode(int current,int size,String status,String code){
        current = size*(current-1);
        Page page;
        int pageSum = orderDetailMapper.allOrderByManagerAndCode(status,code);
        List<OrderDetail> orders = orderDetailMapper.checkPageOrderByManagerAndCode(current,size,status,code);
        page = PageUtils.setPage(pageSum, orders, size);
        return page;
    }
    //查看一个商家的所有订单
    public Page checkAllOrderByBusiness(int bid,int current,int size,String status){
        current = size*(current-1);
        Page page;
        int pageSum = orderDetailMapper.allOrderByBusiness(bid,status);
        List<OrderDetail> orders = orderDetailMapper.checkPageOrderByBusiness(bid,current,size,status);
        page = PageUtils.setPage(pageSum, orders, size);
        return page;
    }

    public Page checkAllOrderByBusinessAndCode(int bid,int current,int size,String status,String code){
        current = size*(current-1);
        Page page;
        int pageSum = orderDetailMapper.allOrderByBusinessAndCode(bid,status,code);
        List<OrderDetail> orders = orderDetailMapper.checkPageOrderByBusinessAndCode(bid,current,size,status,code);
        page = PageUtils.setPage(pageSum, orders, size);
        return page;
    }

    //判断库存够不够
    public String judgeStock(int num,int stock){
        if(num > stock){
            return MessageNews.MESSAGE_ORDER_NUM_OUT_OF_STOCK;
        }
        return MessageNews.MESSAGE_ORDER_NUM_NOT_OUT_OF_STOCK;
    }

    //支付订单
    public boolean payOrder(int id,String type){
        int i = orderMapper.payOrder(id, type);
        Order order = orderMapper.selectById(id);
        int id1 = order.getUserId();
        //修改积分
        User user = userMapper.selectById(id1);
        double score = order.getAmount() + user.getScore();
        int i1 = userMapper.saveScore(id1, score);
        if ( i!=0 ){
            return true;
        }
        return false;
    }

    public boolean payNoOrder(int id, String type) {
        int i = orderMapper.payOrder(id, type);
        Order order = orderMapper.selectById(id);
        if ( i!=0 ){
            return true;
        }
        return false;
    }
}
