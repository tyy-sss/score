package com.example.go.redis;

import com.example.go.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {
    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void test() throws JsonProcessingException {
        User user = new User();
        user.setId(1);
        String s = new ObjectMapper().writeValueAsString(user);
        redisTemplate.opsForValue().set("user",s);
        System.out.println(redisTemplate.opsForValue().get("user"));

    }
}
