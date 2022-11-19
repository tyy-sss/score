package com.example.go.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    private String phone;

    private String sex;

    private String email;

    private String idNumber;//身份证号码

    private String avatar;

    private Integer status;

    private LocalDateTime createTime;

    @TableField(exist = false)
    private String token;

    //邮箱验证码
    @TableField(exist = false)
    private String code;
}
