package com.example.go.controller.collection;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.go.common.R;
import com.example.go.service.collection.CollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 商品评分
 */
@Slf4j
@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    CollectionService collectionService;

    @PostMapping("/saveCollection/{cid}/{uid}/{type}/{collection}")
    public void saveCollection(@PathVariable("cid") int cid, @PathVariable("uid") int uid, @PathVariable("type") String type, @PathVariable("collection") boolean collection) throws ParseException {
        collectionService.saveCollection(cid,uid,type,collection);
    }

    @GetMapping("/getCollection/{cid}/{uid}/{type}")
    public R getCollection(@PathVariable("cid") int cid, @PathVariable("uid") int uid, @PathVariable("type") String type){
        boolean collection = collectionService.getCollection(cid, type, uid);
        return R.success(collection);
    }

    @GetMapping("/checkAllCollection/{id}/{current}/{limit}")
    public R checkAllCollection(@PathVariable Integer id, @PathVariable int current, @PathVariable int limit,@RequestParam(value= "time" ,required = false) String time, @RequestParam(value= "type" ,required = false) String type) throws ParseException {
        if ( !time.isEmpty() ){
            time = time + " 00:00:00";
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = sf.parse(time);
            System.out.println(date);
            Timestamp t1 = new Timestamp(date.getTime());
            Page page = collectionService.checkAllCollection(id,current,limit,t1,type);
            return R.success(page);
        } else {
            Page page = collectionService.checkAllCollection(id, current, limit,null , type);
            return R.success(page);
        }
    }

}
