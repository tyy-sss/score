package com.example.go.Test;

import com.example.go.config.WebSocketConfig;
import com.example.go.mapper.ChatMapper;
import com.example.go.mapper.HotelMapper;
import com.example.go.mapper.RailwayMapper;
import com.example.go.mapper.TicketMapper;
import com.example.go.mapper.TrainMapper;
import com.example.go.mapper.OrderDetailMapper;
import com.example.go.mapper.OrderMapper;
import com.example.go.service.business.BusinessService;
import com.example.go.service.manager.ManagerService;
import com.example.go.service.manager.TicketManagerService;
import com.example.go.service.order.OrderService;
import com.example.go.service.ticket.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TicketTest {

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private RailwayMapper railWayMapper;

    @Autowired
    private TrainMapper trainMapper;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private TicketManagerService ticketManagerService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private HotelMapper hotelMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ChatMapper talkMapper;

    @Autowired(required = false)
    private WebSocketConfig webSocketConfig;

    @Test
    public void test1(){
        talkMapper.allChatNews(1,1);
        System.out.println(ticketService.getAdviceTicket(5));
    }
}
