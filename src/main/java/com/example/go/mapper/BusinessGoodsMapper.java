package com.example.go.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.go.entity.BusinessGoods;
import com.example.go.entity.Manager;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BusinessGoodsMapper extends BaseMapper<BusinessGoods> {

    //把商家和商品联系起来
    int creatContact(int bid,int gid);

    //删除商家与分类表之间的类型
    int deleteContact(int bid,int gid);
}
