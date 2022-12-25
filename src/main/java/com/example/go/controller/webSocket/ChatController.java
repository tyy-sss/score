package com.example.go.controller.webSocket;


import com.example.go.common.R;
import com.example.go.entity.Chat;
import com.example.go.mapper.ChatMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatMapper chatMapper;

    /**
     * 得到当前聊天人的全部消息
     * @param
     * @return
     */
    @GetMapping("/allChat")
    public R allChat(@RequestParam Integer userId,@RequestParam Integer managerId){
        System.out.println(userId+","+managerId);
        List<Chat> chats = chatMapper.allChatNews(userId, managerId);
        return R.success(chats);
    }

}
