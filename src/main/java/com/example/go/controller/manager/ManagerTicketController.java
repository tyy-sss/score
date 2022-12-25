package com.example.go.controller.manager;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.go.common.MessageNews;
import com.example.go.common.R;
import com.example.go.entity.Manager;
import com.example.go.entity.Ticket;
import com.example.go.mapper.TicketMapper;
import com.example.go.service.manager.ManagerService;
import com.example.go.service.manager.TicketManagerService;
import com.example.go.utils.GetNowTime;
import com.example.go.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

/**
 * 管理员管理机票
 */
@Slf4j
@RestController
@RequestMapping("/managerTicket")
public class ManagerTicketController {

    @Autowired
    private TicketManagerService ticketService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private TicketMapper ticketMapper;

    @PostMapping("/add/{id}")
    public R addTicket(@PathVariable Integer id, @RequestBody Ticket ticket) throws ParseException {
        //得到商家
        Manager manager = managerService.isManager(id);
        boolean b = ticketService.addTicket(manager.getId(), ticket);
        //将机票添加到分类表
        //添加商家与分类表的类型
        if( !b ){
            return R.error(MessageNews.MESSAGE_BUSINESS_ADD_TICKET_FAIL);
        }
        return R.success(MessageNews.MESSAGE_BUSINESS_ADD_TICKET_SUCCESS);
    }

    @PostMapping("/changeTicket")
    public R changeTicket(@RequestBody Ticket ticket) throws ParseException {
        System.out.println(ticket);
        Date date = GetNowTime.getNowTime();
        LambdaUpdateWrapper<Ticket> ticketLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        ticketLambdaUpdateWrapper.set(Ticket::getStartPlace,ticket.getStartPlace())
                .set(Ticket::getEndPlace,ticket.getEndPlace())
                .set(Ticket::getStartTime,ticket.getStartTime())
                .set(Ticket::getEndTime,ticket.getEndTime())
                .set(Ticket::getNum,ticket.getNum())
                .set(Ticket::getPrice,ticket.getPrice())
                .set(Ticket::getNum,ticket.getNum())
                .set(Ticket::getDiscount,ticket.getDiscount())
                .set(Ticket::getUpdateTime,date)
                .eq(Ticket::getId,ticket.getId());
        int row = ticketMapper.update(null, ticketLambdaUpdateWrapper);
        if(row != 1){
            return R.error(MessageNews.MESSAGE_MANAGER_CHANGE_TICKET_FAIL);
        }
        return R.success(MessageNews.MESSAGE_MANAGER_CHANGE_TICKET_SUCCESS);
    }

    @DeleteMapping("/deleteTicket/{id}/{token}")
    public R deleteTicket(@PathVariable("id") int id,@PathVariable("token") String token){
        int bid = JwtUtils.getId(token);
        ticketService.deleteTicket(id,bid);
        return R.success(MessageNews.MESSAGE_MANAGER_DELETE_TICKET_SUCCESS);
    }

}
