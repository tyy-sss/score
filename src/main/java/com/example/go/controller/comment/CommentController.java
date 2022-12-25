package com.example.go.controller.comment;

import com.example.go.common.MessageNews;
import com.example.go.common.R;
import com.example.go.entity.Comment;
import com.example.go.service.category.CategoryService;
import com.example.go.service.comment.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 *酒店评论
 */
@Slf4j
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;


    // 发布一级评论
    @PostMapping("/oneComment")
    public R oneComment(@RequestBody Comment comment) throws ParseException {
        System.out.println(comment);
        int categoryIdByGIdAndGType = categoryService.checkGoods(comment.getGoodsId(), comment.getGoodsType());
        boolean oneComment = commentService.createOneComment(comment, categoryIdByGIdAndGType);
        if( oneComment == true){
            return R.success(MessageNews.MESSAGE_SEND_COMMENT_SUCCESS);
        }
        return R.error(MessageNews.MESSAGE_SEND_COMMENT_FAIL);
    }

    //二级评论
    @PostMapping("/twoComment")
    public R secondComment(@RequestBody Comment comment) throws ParseException {
        System.out.println(comment);
        int categoryIdByGIdAndGType = categoryService.checkGoods(comment.getGoodsId(), comment.getGoodsType());
        boolean twoComment = commentService.createTwoComment(comment, categoryIdByGIdAndGType);
        if( twoComment == true){
            return R.success(MessageNews.MESSAGE_SEND_COMMENT_SUCCESS);
        }
        return R.error(MessageNews.MESSAGE_SEND_COMMENT_FAIL);
    }

    //删除评论
    @PostMapping("deleteComment")
    public R deleteComment(@RequestBody Comment comment){
//        System.out.println(comment);
        boolean b = commentService.deleteComment(comment.getId());
        if( b ){
            return R.success(MessageNews.MESSAGE_DELETE_COMMENT_SUCCESS);
        }
        return R.error(MessageNews.MESSAGE_DELETE_COMMENT_FAIL);
    }

    //得到一个商品的所有评论
    @GetMapping("/getAllComment/{goodsId}/{goodsType}")
    public R getAllComment(@PathVariable int goodsId,@PathVariable String goodsType){
        System.out.println(goodsType);
        int categoryIdByGIdAndGType = categoryService.checkGoods( goodsId, goodsType);
        List<Comment> allComment = commentService.getAllComment(categoryIdByGIdAndGType);
        return R.success(allComment);
    }

}
