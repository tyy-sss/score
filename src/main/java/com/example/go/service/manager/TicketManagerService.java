package com.example.go.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.go.entity.Category;
import com.example.go.entity.Ticket;
import com.example.go.mapper.CategoryMapper;
import com.example.go.mapper.BusinessGoodsMapper;
import com.example.go.mapper.TicketMapper;
import com.example.go.service.business.BusinessService;
import com.example.go.service.category.CategoryService;
import com.example.go.utils.GetNowTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class TicketManagerService {
    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private BusinessGoodsMapper businessGoodsMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BusinessService businessService;
    //添加机票
    public boolean addTicket(int id, Ticket ticket) throws ParseException {
        ticket.setCreateTime(GetNowTime.getNowTime());
        ticket.setCreateManager(id);
        ticket.setStock(ticket.getNum());
        //创建机票
        ticketMapper.insert(ticket);
        int gid = ticket.getId();
        //把机票商品放入分类表
        int cid = categoryService.addGoods("机票", gid);
        System.out.println(cid);
        //把商品与分类表连接起来(在ticket中填写分类表的id)
        LambdaUpdateWrapper<Ticket> ticketLambdaUpdateWrapper = new LambdaUpdateWrapper<Ticket>();
        ticketLambdaUpdateWrapper.set(Ticket::getCategoryId,cid);
        ticketLambdaUpdateWrapper.eq(Ticket::getId,gid);
        int row = ticketMapper.update(null,ticketLambdaUpdateWrapper);
        //把商家与分类表连接起来
        int insert=businessService.addBusinessGoods(id,cid);
        if(insert !=0) {
            return true;
        }
        return false;
    }

    //删除机票
    public boolean deleteTicket(int gid,int bid){
        //找到商品在分类表中的位置
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<Category>();
        categoryLambdaQueryWrapper.eq(Category::getProductId,gid)
                .eq(Category::getType,"机票");
        Category category = categoryMapper.selectOne(categoryLambdaQueryWrapper);
        System.out.println(category);
        int cid = category.getId();
        //删除分类表与商家的联系
        businessGoodsMapper.deleteContact(bid,cid);
        //删除商品与分类表的联系
        categoryMapper.deleteContact(gid,"机票");
        //删除ticket
        LambdaQueryWrapper<Ticket> ticketLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ticketLambdaQueryWrapper.eq(Ticket::getId,gid);
        ticketMapper.delete(ticketLambdaQueryWrapper);
        return true;
    }
}
