package com.example.go.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.go.entity.Hotel;

import java.util.List;

public class PageUtils {

    public static Page setPage(int pageSum, List list, int size){
        Page page = new Page();
        page.setTotal(pageSum);
        page.setRecords(list);
        int pages = pageSum/size;
        if(pageSum%size !=0){
            pages += 1;
        }
        page.setPages(pages);
        return page;
    }
}
