package com.example.go.service.chat;

import com.example.go.entity.Chat;
import com.example.go.mapper.ChatMapper;
import com.example.go.utils.GetNowTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class ChatService {

    @Autowired
    private ChatMapper chatMapper;

    public int insert(Chat chat) throws ParseException {
        chat.setTime(GetNowTime.getNowTime());
        int insert = chatMapper.insert(chat);
        return insert;
    }
}
