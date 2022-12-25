package com.example.go.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.go.entity.Chat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMapper extends BaseMapper<Chat> {
    //得到所有的聊天记录
    List<Chat> allChatNews(int userId, int managerId);
}
