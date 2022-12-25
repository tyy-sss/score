package com.example.go.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 管理员 商家
 */
@Data
@Component
public class Manager implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    private String username;

    private String businessName;

    private String businessPlace;

    private String password;

    private String phone;

    private String idNumber;//身份证号码

    private String email;

    private Integer status;

    private Timestamp createTime;

    private Timestamp updateTime;

    private int disable;
    @TableField(exist = false)
    private String token;

    //邮箱验证码
    @TableField(exist = false)
    private String code;
}