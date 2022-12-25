package com.example.go.utils;

import com.example.go.service.order.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 管理订单超时取消并解锁库存的定时器
 */
@Component
@Slf4j
public class OrderTimeOutSender {

//    @Autowired
//    private OrderDetailService orderDetailService;
//
//    //秒 分 小时 日期 月份 星期 年（可选）
//    // 1分钟执行一次
//    @Scheduled(cron="0 0/1 * ? * ?")
//    private void cancelOverTimeOrder(){
//        log.info("---------------订单超时监控--------------");
//        orderDetailService.cancelOverTimeOrder();
//        log.info("---------------订单超时结束--------------");
//    }
}
