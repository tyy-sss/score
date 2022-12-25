package com.example.go.utils;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

@Service
public class MailUtils {
    private static final String SYMBOLS = "0123456789";

    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    //发送邮箱验证码
    public void sendEmail(String email,String code){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("去哪儿验证码");
        simpleMailMessage.setText("尊敬的："+email+"您的验证码是："+code+"有效期为5分钟");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(fromEmail);
        javaMailSender.send(simpleMailMessage);
    }
    /**
     * Math.random生成的是一般随机数，采用的是类似于统计学的随机数生成规则，其输出结果很容易预测，因此可能导致被攻击者击中。
     * 而SecureRandom是真随机数，采用的是类似于密码学的随机数生成规则，其输出结果较难预测，若想要预防被攻击者攻击，最好做到使攻击者根本无法，或不可能鉴别生成的随机值和真正的随机值。
     */
    private static final Random RANDOM = new SecureRandom();

    public static String getCode() {
        char[] nonceChars = new char[6];
        for (int i = 0; i < nonceChars.length; i++) {
            nonceChars[i] = SYMBOLS.charAt(RANDOM.nextInt(nonceChars.length));
        }
        return new String(nonceChars);
    }

    /**
     *计算两个日期的分钟差
     */
    public static int getMinute(Date fromDate, Date toDate) {
        return (int) (toDate.getTime() - fromDate.getTime()) / (5 * 60 * 1000);
    }

}
