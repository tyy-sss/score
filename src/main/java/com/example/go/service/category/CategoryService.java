package com.example.go.service.category;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.go.entity.Category;
import com.example.go.mapper.CategoryMapper;
import com.example.go.utils.GetNowTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    //把商品放入分类表中
    public int addGoods(String name,int pId) throws ParseException {
        Category category = new Category();
        category.setType(name);
        category.setProductId(pId);
        category.setCreateTime(GetNowTime.getNowTime());
        categoryMapper.insert(category);
        return category.getId();
    }

    //找到商品在分类表中的位置
    public int checkGoods(int gid,String name){
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<Category>();
        categoryLambdaQueryWrapper.eq(Category::getProductId,gid)
                .eq(Category::getType,name);
        Category category = categoryMapper.selectOne(categoryLambdaQueryWrapper);
        System.out.println(category);
        return category.getId();
    }

//    //通过商品的id和商品的类型得到商品在分类表中的id
//    public int getCategoryIdByGIdAndGType(int gid,String gType){
//        int categoryIdByGIdAndGType = categoryMapper.getCategoryIdByGIdAndGType(gid, gType);
//        return categoryIdByGIdAndGType;
//    }
    //通过cid得到商品的具体消息
    public Category getCategoryByCid(int cid){
        Category category = categoryMapper.getCategoryByCid(cid);
        return category;
    }
}
