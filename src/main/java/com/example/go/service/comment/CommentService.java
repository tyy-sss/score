package com.example.go.service.comment;

import com.example.go.entity.Category;
import com.example.go.entity.Comment;
import com.example.go.mapper.CommentMapper;
import com.example.go.service.category.CategoryService;
import com.example.go.utils.GetNowTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    CategoryService categoryService;
    //得到一个酒店的所有评论
    public List<Comment> getAllComment(int cId) {
        //得到所有一级评论
        List<Comment> oneComment = commentMapper.getOneComment(cId);
        for (Comment comment : oneComment) {
            //得到一级评论下的子评论
            comment.setChildren(commentMapper.getChildren(comment.getId()));
        }
        return oneComment;
    }

    //创建一个一级评论
    public boolean createOneComment(Comment comment,int cId) throws ParseException {
        comment.setCategoryId(cId);
        comment.setParentId(-1);
        comment.setTime(GetNowTime.getNowTime());
        int insert = commentMapper.insert(comment);
        changeCommentNum(cId);
        if( insert != 0){
            return true;
        }
        return false;
    }

    //创建一个二级评论
    public boolean createTwoComment(Comment comment,int cId) throws ParseException {
        comment.setCategoryId(cId);
        //找到根评论
        int rootId = commentMapper.findRootId(comment.getParentId());
        if (rootId == -1){
            comment.setRootId(comment.getParentId());
        }else {
            comment.setRootId(rootId);
        }
        //找到被评论的人的id
        int commentedId = commentMapper.findCommentedId(comment.getParentId());
        comment.setCommentedId(commentedId);
        comment.setTime(GetNowTime.getNowTime());
        int insert = commentMapper.insert(comment);
        changeCommentNum(cId);
        if( insert != 0){
            return true;
        }
        return false;
    }

    //删除评论
    public boolean  deleteComment(int id){
        int cid = commentMapper.findCid(id);
        int i = commentMapper.deleteComment(id);
        changeCommentNum(cid);
        if( i != 0){
            return true;
        }
        return false;
    }

    //修改评论数
    public void changeCommentNum(int cid){
        //通过评论在分类表id找到商品的种类和id
        Category categoryByCid = categoryService.getCategoryByCid(cid);
        System.out.println("___________________________"+categoryByCid);
        if( categoryByCid.getType().equals("酒店") ){
            commentMapper.changeHotelComment(cid,categoryByCid.getProductId());
        }
    }
}
