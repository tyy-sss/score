package com.example.go.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.go.entity.Attraction;
import com.example.go.entity.Collection;
import com.example.go.entity.Hotel;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface CollectionMapper extends BaseMapper<Collection> {

    //判断之前是否收藏过
    Collection isCollection(int cid,int uid);
    //修改收藏表的数据
    int updateStatus(int cid, int uid, int status, Timestamp time);

    int changeHotelCollectionNum(int cid,int id);

    int allCollectionForUser(int uid,String type);

    List<Hotel> HotelCollectionForUser(int uid,int current,int size);


    int allCollectionForUserByTime(int uid, String type, Timestamp time);

    List<Hotel> HotelCollectionForUserByTime(int uid, Timestamp time,int current,int size);

    List<Attraction> AttractionCollectionForUser(int uid,int current,int size);

    List<Attraction> AttractionCollectionForUserByTime(int uid, Timestamp time,int current,int size);
}
