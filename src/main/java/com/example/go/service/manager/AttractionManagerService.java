package com.example.go.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.go.entity.Attraction;
import com.example.go.mapper.AttractionMapper;
import com.example.go.mapper.CategoryMapper;
import com.example.go.mapper.BusinessGoodsMapper;
import com.example.go.service.business.BusinessService;
import com.example.go.service.category.CategoryService;
import com.example.go.utils.GetNowTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class AttractionManagerService {

    @Autowired
    private AttractionMapper attractionMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private BusinessGoodsMapper businessGoodsMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    public boolean addAttraction(int id, Attraction attraction) throws ParseException {
        System.out.println(id);
        attraction.setCreateTime(GetNowTime.getNowTime());
        attraction.setCreateManager(id);
        attraction.setStock(attraction.getNum());
        //创建酒店
        attractionMapper.insert(attraction);
        int gid = attraction.getId();
        //把酒店商品放入分类表
        int cid = categoryService.addGoods("景点", gid);
        System.out.println(cid);
        //把商品与分类表连接起来(在Hotel中填写分类表的id)
        LambdaUpdateWrapper<Attraction> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Attraction::getCategoryId,cid);
        updateWrapper.eq(Attraction::getId,gid);
        int row = attractionMapper.update(null,updateWrapper);
        //把商家与分类表连接起来
        int insert=businessService.addBusinessGoods(id,cid);
        if(insert !=0) {
            return true;
        }
        return false;
    }

    public boolean deleteAttraction(int gid, Integer bid) {
        //找到商品在分类表中的位置
        int cid = categoryService.checkGoods(gid,"景点");
        //删除分类表与商家的联系
        businessGoodsMapper.deleteContact(bid,cid);
        //删除商品与分类表的联系
        categoryMapper.deleteContact(gid,"景点");
        //删除ticket
        LambdaQueryWrapper<Attraction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Attraction::getId,gid);
        attractionMapper.delete(queryWrapper);
        return true;
    }
}
