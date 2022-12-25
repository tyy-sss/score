package com.example.go.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.go.entity.Hotel;
import com.example.go.mapper.CategoryMapper;
import com.example.go.mapper.HotelMapper;
import com.example.go.mapper.BusinessGoodsMapper;
import com.example.go.service.business.BusinessService;
import com.example.go.service.category.CategoryService;
import com.example.go.utils.GetNowTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class HotelManagerService {

    @Autowired
    private HotelMapper hotelMapper;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BusinessService businessService;
    @Autowired
    private BusinessGoodsMapper businessGoodsMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    //商家添加酒店
    public boolean addHotel(int id, Hotel hotel) throws ParseException {
        hotel.setCreateTime(GetNowTime.getNowTime());
        hotel.setCreateManager(id);
        hotel.setStock(hotel.getNum());
        //创建酒店
        hotelMapper.insert(hotel);
        int gid = hotel.getId();
        //把酒店商品放入分类表
        int cid = categoryService.addGoods("酒店", gid);
        System.out.println(cid);
        //把商品与分类表连接起来(在Hotel中填写分类表的id)
        LambdaUpdateWrapper<Hotel> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Hotel::getCategoryId,cid);
        updateWrapper.eq(Hotel::getId,gid);
        int row = hotelMapper.update(null,updateWrapper);
        //把商家与分类表连接起来
        int insert=businessService.addBusinessGoods(id,cid);
        if(insert !=0) {
            return true;
        }
        return false;
    }

    //删出商家
    public boolean deleteHotel(int gid, Integer bid) {
        //找到商品在分类表中的位置
        int cid = categoryService.checkGoods(gid,"酒店");
        //删除分类表与商家的联系
        businessGoodsMapper.deleteContact(bid,cid);
        //删除商品与分类表的联系
        categoryMapper.deleteContact(gid,"酒店");
        //删除ticket
        LambdaQueryWrapper<Hotel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Hotel::getId,gid);
        hotelMapper.delete(queryWrapper);
        return true;
    }
}
