package com.example.go.controller.point;


import com.example.go.common.R;
import com.example.go.service.point.PointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品评分
 */
@Slf4j
@RestController
@RequestMapping("/point")
public class PointController {

    @Autowired
    public PointService pointService;

    @PostMapping("/savePoint/{cid}/{uid}/{type}/{point}")
    public void savePoint(@PathVariable("cid") int cid,@PathVariable("uid") int uid,@PathVariable("type") String type,@PathVariable("point") int point){
        pointService.savePoint(cid,type,uid,point);
    }

    @GetMapping("/getPoint/{cid}/{uid}/{type}")
    public R getPoint(@PathVariable("cid") int cid,@PathVariable("uid") int uid,@PathVariable("type") String type){
        int point = pointService.getPoint(cid, type, uid);
        return R.success(point);
    }
}
