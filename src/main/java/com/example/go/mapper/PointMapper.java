package com.example.go.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.go.entity.Point;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PointMapper extends BaseMapper<Point> {

    //判断之前是否评分过
    Point isPoint(int cid,int uid);

    //修改评分表
    int updatePointNum(int cid,int uid,int point);

    int changeHotelPoint(int cid,int id);

    int changeAttractionPoint(int cid, int id);
}
