package com.example.go.entity;

import lombok.Data;

@Data
public class ChatPeople {

    private int id;

    private String username;

    private String status;//1是管理员 2是用户

    public ChatPeople(int id, String username, String status) {
        this.id = id;
        this.username = username;
        this.status = status;
    }

    public ChatPeople() {
    }
}
