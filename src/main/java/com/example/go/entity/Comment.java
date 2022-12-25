package com.example.go.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@Component
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    private int categoryId;

    //评论人
    private int commenterId;
    //被评论人
    private int commentedId;

    private int parentId;

    private int rootId;

    private String content;

    private Timestamp time;

    @TableField(exist = false)
    private String commenterName;

    @TableField(exist = false)
    private String commenterImg;

    @TableField(exist = false)
    private String commentedName;

    @TableField(exist = false)
    private List<Comment> children;

    @TableField(exist = false)
    private int goodsId;

    @TableField(exist = false)
    private String goodsType;

}
