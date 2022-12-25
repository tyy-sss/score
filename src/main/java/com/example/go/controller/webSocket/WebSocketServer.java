package com.example.go.controller.webSocket;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.go.entity.Chat;
import com.example.go.entity.ChatPeople;
import com.example.go.entity.Manager;
import com.example.go.entity.User;
import com.example.go.mapper.CategoryMapper;
import com.example.go.service.chat.ChatService;
import com.example.go.service.manager.ManagerService;
import com.example.go.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author websocket服务
 */
@ServerEndpoint(value = "/imserver/{username}/{id}/{status}")
@Component
public class WebSocketServer {
    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    /**
     * 记录当前在线连接数
     */
    public static final Map<ChatPeople, Session> sessionMap = new ConcurrentHashMap<>();

    private static ChatService chatService;

    private static UserService userService;

    private static ManagerService managerService;

    @Autowired
    public void setService(ChatService chatService,UserService userService,ManagerService managerService) {
        WebSocketServer.chatService = chatService;
        WebSocketServer.userService = userService;
        WebSocketServer.managerService = managerService;
    }



    @Autowired
    private CategoryMapper categoryMapper;
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username,@PathParam("id") Integer id,@PathParam("status") String status) throws ParseException {
        ChatPeople chatPeople = new ChatPeople(id,username,status);
        System.out.println(chatPeople);
        System.out.println(session);
        sessionMap.put(chatPeople, session);
        if ( status.equals("2") ){//客服
            //设置用户的上线状态
            userService.updateUpTime(id);
        }
        log.info("有新用户加入，username={}, 当前在线人数为：{}", username, sessionMap.size());
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        result.set("users", array);
        for (Object key : sessionMap.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("user", key);
            // {"user":{"id":6,"username":"11","status":"2"}}
            array.add(jsonObject);
        }
//      {"users":[{"user":{"id":6,"username":"11","status":"2"}}]}
        sendAllMessage(JSONUtil.toJsonStr(result));  // 后台发送消息给所有的客户端
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("username") String username,@PathParam("id") Integer id,@PathParam("status") String status) {
        ChatPeople chatPeople = new ChatPeople(id,username,status);
        sessionMap.remove(chatPeople);
        log.info("有一连接关闭，移除username={}的用户session, 当前在线人数为：{}", username, sessionMap.size());
    }
    /**
     * 收到客户端消息后调用的方法
     * 后台收到客户端发送过来的消息
     * onMessage 是一个消息的中转站
     * 接受 浏览器端 socket.send 发送过来的 json数据
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session,  @PathParam("username") String username,@PathParam("id") Integer id,@PathParam("status") String status) throws ParseException {
        log.info("服务端收到用户username={}的消息:{}", username, message);
        JSONObject obj = JSONUtil.parseObj(message);
        String toUsername = obj.getStr("to"); // to表示发送给哪个用户，比如 admin
        String text = obj.getStr("text"); // 发送的消息文本  hello
        //创建聊天对象
        Chat chat = new Chat();
        chat.setContent(text);
        ChatPeople chatPeople = new ChatPeople();
        if ( status.equals("2") ){//客服
            Manager managerByUsername = managerService.getManagerByUsername(toUsername);
            chatPeople.setId(managerByUsername.getId());
            chatPeople.setStatus("1");
            chatPeople.setUsername(managerByUsername.getUsername());
            chat.setManagerId(chatPeople.getId());
            chat.setUserId(id);
            chat.setStatus("1");
        }else{//用户
            User userByUsername = userService.getUserByUsername(toUsername);
            chatPeople.setId(userByUsername.getId());
            chatPeople.setStatus("2");
            chatPeople.setUsername(userByUsername.getUsername());
            chat.setUserId(chatPeople.getId());
            chat.setManagerId(id);
            chat.setStatus("0");
        }
        System.out.println("聊天："+chat);
        //查入聊天对象
        int insert = chatService.insert(chat);
        System.out.println("要发送消息的对象："+chatPeople);
        Session toSession = sessionMap.get(chatPeople); // 根据 to用户名来获取 session，再通过session发送消息文本
        System.out.println(toSession);
        if (toSession != null) {
            // 服务器端 再把消息组装一下，组装后的消息包含发送人和发送的文本内容
            // {"from": "zhang", "text": "hello"}
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("from", username);  // from 是 zhang
            jsonObject.set("text", text);  // text 同上面的text
            this.sendMessage(jsonObject.toString(), toSession);
            log.info("发送给用户username={}，消息：{}", toUsername, jsonObject.toString());
        } else {
            log.info("发送失败，未找到用户username={}的session", toUsername);
        }
    }
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }
    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }
    /**
     * 服务端发送消息给所有客户端
     */
    private void sendAllMessage(String message) {
        try {
            for (Session session : sessionMap.values()) {
                log.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }
}
