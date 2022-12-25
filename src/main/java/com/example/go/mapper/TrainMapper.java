package com.example.go.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.go.entity.Train;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TrainMapper extends BaseMapper<Train> {
    int allTrainForBusiness(int businessId);

    List<Train> pageTrainForBusiness(int businessId, int current, int size);
}
