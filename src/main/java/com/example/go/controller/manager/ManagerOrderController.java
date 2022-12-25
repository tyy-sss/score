package com.example.go.controller.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.go.common.R;
import com.example.go.entity.Manager;
import com.example.go.entity.OrderDetail;
import com.example.go.mapper.ManagerMapper;
import com.example.go.service.order.OrderDetailService;
import com.example.go.service.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/managerOrder")
public class ManagerOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    public OrderDetailService orderDetailService;

    @Autowired
    public ManagerMapper managerMapper;

    @GetMapping("/checkAllOrder/{id}/{current}/{limit}/{status}")
    public R checkAllOrder(@PathVariable int id,@PathVariable int current, @PathVariable int limit, @PathVariable("status") String status,@RequestParam(value= "name" ,required = false) String name)throws ParseException {
        //判断现在是商家还是管理员
        Manager manager = managerMapper.selectById(id);
        Page page = null;
        System.out.println("-------------------------查看订单------------------");
        if ( manager.getStatus() == 0 ){//管理员
            if ( name.isEmpty() ){
                page = orderService.checkAllOrderByManager(current, limit, status);
            }else{
                page = orderService.checkAllOrderByManagerAndCode(current, limit, status, name);
            }
        }else{//商家
            if( name.isEmpty() ){
                page = orderService.checkAllOrderByBusiness(id,current, limit, status);
            }else{
                page = orderService.checkAllOrderByBusinessAndCode(id,current, limit, status, name);
            }
        }
        return R.success(page);
    }

    //根据订单号得到订单的具体消息
    @GetMapping("/getOrderNews/{id}")
    public R getOrderNews(@PathVariable("id") int id){
        System.out.println(id);
        OrderDetail orderNews = orderDetailService.getOrderNews(id);
        return R.success(orderNews);
    }
}
