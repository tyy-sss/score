package com.example.go.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.go.entity.Railway;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RailwayMapper extends BaseMapper<Railway> {
    int allRailwayForBusiness(int businessId);

    List<Railway> pageRailwayForBusiness(int businessId, int current, int size);
}
