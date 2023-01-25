package com.example.go.service.point;

import com.example.go.entity.Point;
import com.example.go.mapper.PointMapper;
import com.example.go.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    @Autowired
    public CategoryService categoryService;

    @Autowired
    public PointMapper pointMapper;

    public void savePoint(int gid,String type,int uid,int point){
        int cid = categoryService.checkGoods(gid, type);
        if( pointMapper.isPoint(cid,uid) == null ){//没有评过分
            Point point1 = new Point();
            point1.setCategoryId(cid);
            point1.setPointNum(point);
            point1.setUserId(uid);
            int insert = pointMapper.insert(point1);
        } else {//修改评分
            int i = pointMapper.updatePointNum(cid, uid, point);
        }
        changePoint(cid,gid,type);
    }

    public int getPoint(int gid,String type,int uid){
        int cid = categoryService.checkGoods(gid, type);
        Point point = pointMapper.isPoint(cid, uid);
        if( point == null ){
            return 0;
        }
        return point.getPointNum();
    }

    public void changePoint(int cid,int id,String type){
        if( type.equals("酒店") ){
            pointMapper.changeHotelPoint(cid,id);
        }else if(type.equals("景点")){
            pointMapper.changeAttractionPoint(cid,id);
        }
    }

}
