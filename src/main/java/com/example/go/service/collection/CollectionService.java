package com.example.go.service.collection;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.go.entity.*;
import com.example.go.mapper.CollectionMapper;
import com.example.go.service.category.CategoryService;
import com.example.go.utils.GetNowTime;
import com.example.go.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

@Service
public class CollectionService {

    @Autowired
    CollectionMapper collectionMapper;

    @Autowired
    CategoryService categoryService;

    public void saveCollection(int gid, int uid, String type, boolean collection) throws ParseException {
        int cid = categoryService.checkGoods(gid, type);
        int status = 0 ;
        if (collection == true){
            status = 1;
        }
        if ( collectionMapper.isCollection(cid,uid) == null ){
            Collection collection1 = new Collection();
            collection1.setCategoryId(cid);
            collection1.setUserId(uid);
            collection1.setStatus(status);
            collection1.setTime(GetNowTime.getNowTime());
            collectionMapper.insert(collection1);
        }else{
            collectionMapper.updateStatus(cid,uid,status,GetNowTime.getNowTime());
        }
        changeCollection(cid,gid,type);
    }

    public boolean getCollection(int gid,String type,int uid){
        int cid = categoryService.checkGoods(gid, type);
        Collection collection = collectionMapper.isCollection(cid, uid);
        if( collection == null || collection.getStatus() == 0){
            return false;
        }
        return true;
    }

    public void changeCollection(int cid,int id,String type){
        if( type.equals("酒店") ){
            collectionMapper.changeHotelCollectionNum(cid,id);
        }else if ( type.equals("景点") ){
            collectionMapper.changeAttractionCollectionNum(cid,id);
        }
    }

    public Page checkAllCollection(int uid, int current, int size, Timestamp time, String type){
        Page page;
        current = size*(current-1);
        if( type.equals("酒店") ){
            if (time == null){
                int pageSum = collectionMapper.allCollectionForUser(uid,type);
                List<Hotel> hotelList = collectionMapper.HotelCollectionForUser(uid,current,size);
                page = PageUtils.setPage(pageSum,hotelList,size);
                return page;
            }else{
                int pageSum = collectionMapper.allCollectionForUserByTime(uid,type,time);
                List<Hotel> hotelList = collectionMapper.HotelCollectionForUserByTime(uid,time,current,size);
                page = PageUtils.setPage(pageSum,hotelList,size);
                return page;
            }
        }else{
            if( time == null ){
                int pageSum = collectionMapper.allCollectionForUser(uid,type);
                List<Attraction> attractionList = collectionMapper.AttractionCollectionForUser(uid,current,size);
                page = PageUtils.setPage(pageSum,attractionList,size);
                return page;
            }else{
                int pageSum = collectionMapper.allCollectionForUserByTime(uid,type,time);
                List<Attraction> attractionList = collectionMapper.AttractionCollectionForUserByTime(uid,time,current,size);
                page = PageUtils.setPage(pageSum,attractionList,size);
                return page;
            }
        }
    }

}
