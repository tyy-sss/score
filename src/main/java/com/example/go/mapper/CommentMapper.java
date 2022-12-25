package com.example.go.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.go.entity.Comment;
import com.example.go.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 评论
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    //获得没有子评论的评论
    List<Comment> getOneComment(int cId);
    //获得一个根评论的所有子评论
    List<Comment> getChildren(int id);

    //删除评论
    int deleteComment(int id);
    //找到根评论
    int findRootId(int id);
    //找到被评论人的id
    int findCommentedId(int id);
    //找到分类表的id
    int findCid(int id);
    int changeHotelComment(int cid, int id);
}
