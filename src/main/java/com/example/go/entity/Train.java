package com.example.go.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 火车票
 */
@Data
public class Train implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    private int categoryId;

    private String startPlace;

    private String endPlace;

    private Timestamp startTime;

    private Timestamp endTime;

    private int stock;

    private int num;//总量

    private Double price;//原价

    private Double discount;

    @TableField(exist = false)
    private int people_num;//售出票数

    private Timestamp createTime;

    private Timestamp updateTime;

    private int createManager;

    @TableField(exist = false)
    private String businessName;

    @TableField(exist = false)
    private String businessPlace;

}
