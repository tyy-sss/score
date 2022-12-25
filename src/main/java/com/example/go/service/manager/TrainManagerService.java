package com.example.go.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.go.entity.Category;
import com.example.go.entity.Train;
import com.example.go.mapper.CategoryMapper;
import com.example.go.mapper.BusinessGoodsMapper;
import com.example.go.mapper.TrainMapper;
import com.example.go.service.business.BusinessService;
import com.example.go.service.category.CategoryService;
import com.example.go.utils.GetNowTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class TrainManagerService {

    @Autowired
    private TrainMapper trainMapper;
    @Autowired
    private BusinessGoodsMapper businessGoodsMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BusinessService businessService;
    public boolean addTrain(int id, Train train) throws ParseException {
        train.setCreateTime(GetNowTime.getNowTime());
        train.setCreateManager(id);
        train.setStock(train.getNum());
        //创建机票
        trainMapper.insert(train);
        int gid = train.getId();
        //把机票商品放入分类表
        int cid = categoryService.addGoods("火车", gid);
        System.out.println(cid);
        //把商品与分类表连接起来(在railway中填写分类表的id)
        LambdaUpdateWrapper<Train> railwayLambdaUpdateWrapper = new LambdaUpdateWrapper<Train>();
        railwayLambdaUpdateWrapper.set(Train::getCategoryId,cid);
        railwayLambdaUpdateWrapper.eq(Train::getId,gid);
        int row = trainMapper.update(null,railwayLambdaUpdateWrapper);
        //把商家与分类表连接起来
        int insert=businessService.addBusinessGoods(id,cid);
        if(insert !=0) {
            return true;
        }
        return false;
    }

    public boolean deleteTrain(int gid,int bid){
        //找到商品在分类表中的位置
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<Category>();
        categoryLambdaQueryWrapper.eq(Category::getProductId,gid)
                .eq(Category::getType,"火车");
        Category category = categoryMapper.selectOne(categoryLambdaQueryWrapper);
        System.out.println(category);
        int cid = category.getId();
        //删除分类表与商家的联系
        businessGoodsMapper.deleteContact(bid,cid);
        //删除商品与分类表的联系
        categoryMapper.deleteContact(gid,"高铁");
        //删除railway
        LambdaQueryWrapper<Train> trainLambdaQueryWrapper = new LambdaQueryWrapper<>();
        trainLambdaQueryWrapper.eq(Train::getId,gid);
        trainMapper.delete(trainLambdaQueryWrapper);
        return true;
    }
}
