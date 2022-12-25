package com.example.go.service.ticket;

import com.example.go.entity.Ticket;
import com.example.go.mapper.TicketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketMapper ticketMapper;

    //推荐机票
    public List<Ticket> getAdviceTicket(int limit){
        List<Ticket> adviceTicket = ticketMapper.getAdviceTicket(limit);
        return adviceTicket;
    }

    public Ticket getTicketNews(int id) {
        Ticket ticketNews = ticketMapper.getTicketNews(id);
        return ticketNews;
    }
}
