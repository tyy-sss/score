package com.example.go.service.attraction;

import com.example.go.entity.Attraction;
import com.example.go.mapper.AttractionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttractionService {

    @Autowired
    private AttractionMapper attractionMapper;
    
    public List<Attraction> getAdviceHotAttraction(int num) {
        List<Attraction> attractionList=attractionMapper.getAdviceHotAttraction(num);
        return attractionList;
    }

    public List<Attraction> getAdviceCheapAttraction(int begin,int num) {
        List<Attraction> attractionList=attractionMapper.getAdviceCheapAttraction(begin,num);
        return attractionList;
    }

    public Attraction getAttractionNews(int id) {
        Attraction attraction = attractionMapper.getAttractionNews(id);
        return attraction;
    }
}
