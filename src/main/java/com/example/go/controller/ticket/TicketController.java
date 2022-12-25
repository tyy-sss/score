package com.example.go.controller.ticket;

import com.example.go.common.R;
import com.example.go.entity.Hotel;
import com.example.go.entity.Ticket;
import com.example.go.service.manager.TicketManagerService;
import com.example.go.service.ticket.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 机票搜索
 */
@Slf4j
@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    //得到推荐机票
    @GetMapping("/adviceTicket/{num}")
    public R getTicket(@PathVariable("num") int num){
        System.out.println(num);
        List<Ticket> adviceTicket = ticketService.getAdviceTicket(num);
        System.out.println(adviceTicket);
        return R.success(adviceTicket);
    }

    //得到酒店的具体消息
    @GetMapping("/ticketNews/{id}")
    public R getHotelNews(@PathVariable("id") int id){
//        System.out.println(id);
        Ticket ticketNews = ticketService.getTicketNews(id);
        System.out.println(ticketNews);
        return R.success(ticketNews);
    }

}
