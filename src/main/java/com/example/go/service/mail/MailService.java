package com.example.go.service.mail;

import com.example.go.common.MessageNews;
import com.example.go.common.R;
import com.example.go.entity.Manager;
import com.example.go.entity.User;
import com.mysql.cj.util.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MailService {

    @Resource
    private RedisTemplate redisTemplate;

    public String codeJudge(User user){
        //判断验证码是否过期
        String code = (String) redisTemplate.opsForValue().get(user.getEmail());
        if( StringUtils.isNullOrEmpty(code) ){
            //验证码已经发送过，且没有过有效期
            return MessageNews.MESSAGE_EMAIL_CODE_OVERTIME;
        } else if( !code.equals(user.getCode()) ){
            return MessageNews.MESSAGE_EMAIL_CODE_WRONG;
        }
        return null;
    }

    public String codeJudge(Manager manager){
        //判断验证码是否过期
        String code = (String) redisTemplate.opsForValue().get(manager.getEmail());
        if( StringUtils.isNullOrEmpty(code) ){
            //验证码已经发送过，且没有过有效期
            return MessageNews.MESSAGE_EMAIL_CODE_OVERTIME;
        } else if( !code.equals(manager.getCode()) ){
            return MessageNews.MESSAGE_EMAIL_CODE_WRONG;
        }
        return null;
    }
}
