package com.example.go.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Component
@TableName(value = "order_detail")
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private int orderId;

    private int categoryId;

    private String userName;

    private String userPhone;

    private String businessName;

    private String businessPlace;

    private String goodsName;

    private int goodsNum;

    private String goodsPrice;

    private double actualPrice;

    private double total;

    private Timestamp startTime;

    private Timestamp endTime;

    private String startPlace;

    private String endPlace;

    @TableField(exist = false)
    private String code;//订单编号

    @TableField(exist = false)
    private Timestamp orderTime;//订单编号

    @TableField(exist = false)
    private String token;

    @TableField(exist = false)
    private int id;//商品在该商品表中的位置
}
