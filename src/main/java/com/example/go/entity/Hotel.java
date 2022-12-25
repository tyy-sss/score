package com.example.go.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 酒店表
 */
@Data
@Component
public class Hotel implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    private int categoryId;

    private String name;

    private String img;

    private String description;

    private int stock;

    private int num;

    private double price;

    private double discount;

    private int collectionNum;

    private int commentNum;

    private Double point;

    private Timestamp startTime;

    private Timestamp endTime;

    private Timestamp createTime;

    private Timestamp updateTime;

    private int createManager;

    private int updateManager;

    @TableField(exist = false)
    private String businessName;

    @TableField(exist = false)
    private String businessPlace;
}
