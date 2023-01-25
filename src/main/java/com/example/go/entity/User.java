package com.example.go.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 用户
 */
@Data
@Component
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    private String username;

    private String name;

    private String password;

    private double score;//积分

    private String phone;

    private String sex;

    private String email;

    private String idNumber;//身份证号码

    private String avatar;

    private int age;

    private Timestamp birthday;

    private Integer status;

    private double account;

    private Timestamp createTime;

    //最近一次上线时间
    private Timestamp upTime;

    @TableField(exist = false)
    private String token;

    //邮箱验证码
    @TableField(exist = false)
    private String code;
}
