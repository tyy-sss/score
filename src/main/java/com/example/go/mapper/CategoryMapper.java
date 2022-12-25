package com.example.go.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.go.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    //删除商品与分类表的联系
    int deleteContact(int pid,String type);
    //通过商品id和商品类型得到商品在分类表中的id
    int getCategoryIdByGIdAndGType(int gId,String gType);

    Category getCategoryByCid(int cid);
}
