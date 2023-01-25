package com.example.go.controller.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.go.common.MessageNews;
import com.example.go.common.R;
import com.example.go.entity.OrderDetail;
import com.example.go.service.order.OrderDetailService;
import com.example.go.service.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    public OrderDetailService orderDetailService;

    @GetMapping("/checkAllOrder/{id}/{current}/{limit}/{status}")
    public R checkAllOrder(@PathVariable Integer id, @PathVariable int current, @PathVariable int limit,@PathVariable("status") String status,@RequestParam(value= "time" ,required = false) String time, @RequestParam(value= "type" ,required = false) String type) throws ParseException {
        System.out.println(id);
        System.out.println(status);
        if ( !time.isEmpty() ){
            time = time + " 00:00:00";
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = sf.parse(time);
            System.out.println(date);
            Timestamp t1 = new Timestamp(date.getTime());
            Page page = orderService.checkAllOrder(id, current, limit, status , t1, type);
            return R.success(page);
        } else {
            Page page = orderService.checkAllOrder(id, current, limit,status,null , type);
            return R.success(page);
        }
    }

    //根据订单号得到订单的具体消息
    @GetMapping("/getOrderNews/{id}")
    public R getOrderNews(@PathVariable("id") int id){
        System.out.println(id);
        OrderDetail orderNews = orderDetailService.getOrderNews(id);
        return R.success(orderNews);
    }

    @GetMapping("/getAttractionOrderNews/{id}")
    public R getAttractionOrderNews(@PathVariable("id") int id){
        List<OrderDetail> orderNews = orderDetailService.getAttractionOrderNews(id);
        return R.success(orderNews);
    }
    //支付订单
    @PostMapping("/payOrder/{id}/{type}")
    public R payOrder(@PathVariable("id") int id,@PathVariable("type") String type){
        boolean b = orderService.payOrder(id, type);
        if ( b ){
            return R.success(MessageNews.MESSAGE_PAY_ORDER_SUCCESS);
        }
        return R.error(MessageNews.MESSAGE_PAY_ORDER_FAIL);
    }

}
