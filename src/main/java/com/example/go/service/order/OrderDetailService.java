package com.example.go.service.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.go.common.MessageNews;
import com.example.go.common.R;
import com.example.go.entity.*;
import com.example.go.mapper.*;
import com.example.go.service.category.CategoryService;
import com.example.go.service.user.UserService;
import com.example.go.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

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
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AttractionMapper attractionMapper;
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

    //积分商城订单
    public R createOrderIntegral(OrderDetail orderDetail)throws ParseException{
        //判断库存还够不够
        Hotel hotelNews = hotelMapper.getHotelNews(orderDetail.getId());
        String s = orderService.judgeStock(orderDetail.getGoodsNum(), hotelNews.getStock());
        if( s.equals(MessageNews.MESSAGE_ORDER_NUM_OUT_OF_STOCK) ){
            return R.success(s);
        }
        //判断积分够不够
        Integer id = JwtUtils.getId(orderDetail.getToken());
        User user = userMapper.selectById(id);
        if ( user.getScore() <  orderDetail.getTotal()){
            return R.success(MessageNews.MESSAGE_POINT_NOT_ENOUGH);
        }
        //减积分
        double score = user.getScore()-orderDetail.getTotal();
        System.out.println("剩余积分：" + score);
        LambdaUpdateWrapper<User> userLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        userLambdaUpdateWrapper.set(User::getScore,score)
                        .eq(User::getId,id);
        userMapper.update(null,userLambdaUpdateWrapper);
        //库存足够就可以下单
        //得到订单号
        int i = orderService.createOrder(orderDetail);
        //支付成功
        boolean k = orderService.payNoOrder(i, "积分");
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

    //创造景点订单
    public R createOrderAttraction(OrderDetail ticketOrder, OrderDetail hotelOrder, OrderDetail attractionOrder) throws ParseException {
        Ticket ticketNews = ticketMapper.getTicketNews(ticketOrder.getId());
        String s = orderService.judgeStock(ticketOrder.getGoodsNum(), ticketNews.getStock());
        if( s.equals(MessageNews.MESSAGE_ORDER_NUM_OUT_OF_STOCK) ){
            return R.success(s);
        }
        //库存足够就可以下单
        //得到订单号
        int i = orderService.createOrder(ticketOrder);
//        //创建订单详情表
        ticketOrder.setOrderId(i);
        //得到商品在分类表中的位置
        int cid = categoryService.checkGoods(ticketOrder.getId(), "机票");
        ticketOrder.setCategoryId(cid);
        orderDetailMapper.insert(ticketOrder);
        //减少库存，找到商品的位置
        LambdaUpdateWrapper<Ticket> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Ticket::getStock,ticketNews.getStock()-ticketOrder.getGoodsNum())
                .eq(Ticket::getId,ticketNews.getId());
        ticketMapper.update(null, updateWrapper);

        //其他两个商品要用同一个订单编号
        Hotel hotelNews = hotelMapper.getHotelNews(hotelOrder.getId());
        String s1 = orderService.judgeStock(hotelOrder.getGoodsNum(), hotelNews.getStock());
        if( s1.equals(MessageNews.MESSAGE_ORDER_NUM_OUT_OF_STOCK) ) {
            return R.success(s1);
        }
        //修改订单的总值
        orderMapper.changeAmount(hotelOrder.getTotal()+ticketOrder.getTotal(),i);
        hotelOrder.setOrderId(i);
        //得到商品在分类表中的位置
        int hcid = categoryService.checkGoods(hotelOrder.getId(), "酒店");
        hotelOrder.setCategoryId(hcid);
        orderDetailMapper.insert(hotelOrder);
        //减少库存，找到商品的位置
        LambdaUpdateWrapper<Hotel> updateWrapper1 = new LambdaUpdateWrapper<>();
        updateWrapper1.set(Hotel::getStock,hotelNews.getStock()-hotelOrder.getGoodsNum())
                .eq(Hotel::getId,hotelNews.getId());
        hotelMapper.update(null, updateWrapper1);

        //景点
        Attraction attractionNews = attractionMapper.getAttractionNews(attractionOrder.getId());
        String s2 = orderService.judgeStock(attractionOrder.getGoodsNum(), attractionNews.getStock());
        if( s2.equals(MessageNews.MESSAGE_ORDER_NUM_OUT_OF_STOCK) ) {
            return R.success(s2);
        }
        //修改订单的总值
        orderMapper.changeAmount(attractionOrder.getTotal()+hotelOrder.getTotal()+ticketOrder.getTotal(),i);
        attractionOrder.setOrderId(i);
        //得到商品在分类表中的位置
        int acid = categoryService.checkGoods(hotelOrder.getId(), "景点");
        attractionOrder.setCategoryId(acid);
        orderDetailMapper.insert(attractionOrder);
        //减少库存，找到商品的位置
        LambdaUpdateWrapper<Attraction> updateWrapper2 = new LambdaUpdateWrapper<>();
        updateWrapper2.set(Attraction::getStock,attractionNews.getStock()-attractionOrder.getGoodsNum())
                .eq(Attraction::getId,attractionNews.getId());
        attractionMapper.update(null, updateWrapper2);
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

    public List<OrderDetail> getAttractionOrderNews(int id) {
        return orderDetailMapper.getAttractionOrderNews(id);
    }

}
