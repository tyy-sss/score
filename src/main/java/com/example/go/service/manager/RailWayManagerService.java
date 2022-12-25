package com.example.go.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.go.entity.Category;
import com.example.go.entity.Railway;
import com.example.go.mapper.CategoryMapper;
import com.example.go.mapper.BusinessGoodsMapper;
import com.example.go.mapper.RailwayMapper;
import com.example.go.service.business.BusinessService;
import com.example.go.service.category.CategoryService;
import com.example.go.utils.GetNowTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class RailWayManagerService {

    @Autowired
    private RailwayMapper railwayMapper;
    @Autowired
    private BusinessGoodsMapper businessGoodsMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BusinessService businessService;
    public boolean addRailway(int id, Railway railway) throws ParseException {
        railway.setCreateTime(GetNowTime.getNowTime());
        railway.setCreateManager(id);
        railway.setStock(railway.getNum());
        //创建机票
        railwayMapper.insert(railway);
        int gid = railway.getId();
        //把机票商品放入分类表
        int cid = categoryService.addGoods("高铁", gid);
        System.out.println(cid);
        //把商品与分类表连接起来(在railway中填写分类表的id)
        LambdaUpdateWrapper<Railway> railwayLambdaUpdateWrapper = new LambdaUpdateWrapper<Railway>();
        railwayLambdaUpdateWrapper.set(Railway::getCategoryId,cid);
        railwayLambdaUpdateWrapper.eq(Railway::getId,gid);
        int row = railwayMapper.update(null,railwayLambdaUpdateWrapper);
        //把商家与分类表连接起来
        int insert=businessService.addBusinessGoods(id,cid);
        if(insert !=0) {
            return true;
        }
        return false;
    }

    public boolean deleteRailway(int gid,int bid){
        //找到商品在分类表中的位置
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<Category>();
        categoryLambdaQueryWrapper.eq(Category::getProductId,gid)
                .eq(Category::getType,"高铁");
        Category category = categoryMapper.selectOne(categoryLambdaQueryWrapper);
        System.out.println(category);
        int cid = category.getId();
        //删除分类表与商家的联系
        businessGoodsMapper.deleteContact(bid,cid);
        //删除商品与分类表的联系
        categoryMapper.deleteContact(gid,"高铁");
        //删除railway
        LambdaQueryWrapper<Railway> railwayLambdaQueryWrapper = new LambdaQueryWrapper<>();
        railwayLambdaQueryWrapper.eq(Railway::getId,gid);
        railwayMapper.delete(railwayLambdaQueryWrapper);
        return true;
    }
}
