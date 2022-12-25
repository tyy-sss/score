package com.example.go.service.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.go.common.MessageNews;
import com.example.go.common.R;
import com.example.go.entity.Hotel;
import com.example.go.entity.Order;
import com.example.go.entity.OrderDetail;
import com.example.go.entity.Ticket;
import com.example.go.mapper.HotelMapper;
import com.example.go.mapper.TicketMapper;
import com.example.go.mapper.OrderDetailMapper;
import com.example.go.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class OrderDetailService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private HotelMapper hotelMapper;

    @Autowired
    private TicketMapper ticketMapper;
    //创建酒店订单
    public R createOrderHotel(OrderDetail orderDetail) throws ParseException {
        //判断库存还够不够
        Hotel hotelNews = hotelMapper.getHotelNews(orderDetail.getId());
        String s = orderService.judgeStock(orderDetail.getGoodsNum(), hotelNews.getStock());
        if( s.equals(MessageNews.MESSAGE_ORDER_NUM_OUT_OF_STOCK) ){
            return R.success(s);
        }
        //库存足够就可以下单
        //得到订单号
        int i = orderService.createOrder(orderDetail);
//        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
//        //创建订单详情表
        orderDetail.setOrderId(i);
        //得到商品在分类表中的位置
        int cid = categoryService.checkGoods(orderDetail.getId(), "酒店");
        orderDetail.setCategoryId(cid);
        orderDetailMapper.insert(orderDetail);
        //减少库存，找到商品的位置
        LambdaUpdateWrapper<Hotel> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Hotel::getStock,hotelNews.getStock()-orderDetail.getGoodsNum())
                .eq(Hotel::getId,hotelNews.getId());
        hotelMapper.update(null, updateWrapper);
        return R.success(i);
    }

    //创造机票订单
    public R createOrderTicket(OrderDetail orderDetail) throws ParseException {
        //判断库存还够不够
        Ticket ticketNews = ticketMapper.getTicketNews(orderDetail.getId());
        System.out.println(ticketNews.getStock());
        System.out.println(orderDetail.getGoodsNum());
        String s = orderService.judgeStock(orderDetail.getGoodsNum(), ticketNews.getStock());
        if( s.equals(MessageNews.MESSAGE_ORDER_NUM_OUT_OF_STOCK) ){
            return R.success(s);
        }
        //库存足够就可以下单
        //得到订单号
        int i = orderService.createOrder(orderDetail);
//        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
//        //创建订单详情表
        orderDetail.setOrderId(i);
        //得到商品在分类表中的位置
        int cid = categoryService.checkGoods(orderDetail.getId(), "机票");
        orderDetail.setCategoryId(cid);
        orderDetailMapper.insert(orderDetail);
        //减少库存，找到商品的位置
        LambdaUpdateWrapper<Ticket> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Ticket::getStock,ticketNews.getStock()-orderDetail.getGoodsNum())
                .eq(Ticket::getId,ticketNews.getId());
        ticketMapper.update(null, updateWrapper);
        return R.success(i);
    }

    //通过订单id查询订单的具体消息
    public OrderDetail getOrderNews(int id){
        return orderDetailMapper.getOrderNews(id);
    }

    //自动取消规定时间段内未支付的订单
    public void cancelOverTimeOrder() {
        //1。获取规定的时间
        Integer time = 30 * 60 * 1000;
//        DateUtil.offset(new Date(), DateField.MINUTE,time);
        //2.获取超过规定时间内未支付的订单
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getStatus,"1");//未支付
//                .lt(Order::getOrderTime())//是否超时
        //3。改变状态 ： 取消
        //4.添加库存
        //3。改变状态 ： 取消
        //4.添加库存
    }
}
