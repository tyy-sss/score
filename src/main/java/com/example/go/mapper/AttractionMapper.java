package com.example.go.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.go.entity.Attraction;
import com.example.go.entity.Hotel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AttractionMapper  extends BaseMapper<Attraction>  {
    int allAttractionForBusiness(int businessId);

    List<Attraction> pageAttractionForBusiness(int businessId, int current, int size);

    List<Attraction> getAdviceHotAttraction(int num);

    List<Attraction> getAdviceCheapAttraction(int begin,int num);

    Attraction getAttractionNews(int id);
}
