package com.example.go.controller.order;

import com.example.go.common.R;
import com.example.go.entity.OrderDetail;
import com.example.go.service.order.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * 酒店下单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderGoodsController {

    @Autowired
    public OrderDetailService orderDetailService;

    @PostMapping("/orderHotel")
    public R orderHotel(@RequestBody OrderDetail orderDetail) throws ParseException {
        System.out.println(orderDetail);
        return orderDetailService.createOrderHotel(orderDetail);
    }

    @PostMapping("/orderTicket")
    public R orderTicket(@RequestBody OrderDetail orderDetail) throws ParseException {
        System.out.println(orderDetail+"orderDetail");
        return orderDetailService.createOrderTicket(orderDetail);
    }
}
