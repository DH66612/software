package com.echo.service;

import com.echo.dao.ArticleDao;
import com.echo.dao.ArticleDaoImpl;
import com.echo.dao.CommentDao;
import com.echo.dao.CommentDaoImpl;
import com.echo.entity.Comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentServiceImpl implements CommentService {

    private CommentDao commentDao;

    public CommentServiceImpl() {
        this.commentDao = new CommentDaoImpl();
    }




    @Override
    public Comment publishComment(Comment comment) {
        System.out.println("CommentService - 发布评论: " + comment);

        if (comment.getArticleId() == null) {
            throw new RuntimeException("文章ID不能为空");
        }
        if (comment.getUserId() == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new RuntimeException("评论内容不能为空");
        }

        try {
            // 设置创建和更新时间
            comment.setCreateTime(new Date());
            comment.setUpdateTime(new Date());

            int result = commentDao.insert(comment);
            System.out.println("CommentService - 插入结果: " + result);

            if (result > 0) {
                return comment;
            } else {
                throw new RuntimeException("评论发布失败");
            }
        } catch (Exception e) {
            System.out.println("CommentService - 发布评论异常: " + e.getMessage());
            throw new RuntimeException("评论发布失败: " + e.getMessage());
        }
    }
    @Override

    public List<Comment> getCommentsByArticleId(Integer articleId, int page, int pageSize) {
        System.out.println("CommentServiceImpl - 获取文章评论: articleId=" + articleId + ", page=" + page + ", pageSize=" + pageSize);

        if (articleId == null) {
            System.out.println("articleId为null");
            return new ArrayList<>();
        }

        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;

        int offset = (page - 1) * pageSize;

        List<Comment> comments = commentDao.findByArticleId(articleId, offset, pageSize);
        System.out.println("从DAO获取的评论数量: " + comments.size());

        return comments;
    }



    @Override
    public int getCommentCountByArticleId(Integer articleId) {
        if (articleId == null) {
            return 0;
        }

        return commentDao.countByArticleId(articleId);
    }

    @Override
    public boolean deleteComment(Integer id) {
        if (id == null) {
            return false;
        }

        try {
            int result = commentDao.delete(id);
            return result > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateComment(Comment comment) {
        if (comment == null || comment.getId() == null) {
            return false;
        }

        try {
            int result = commentDao.update(comment);
            return result > 0;
        } catch (Exception e) {
            return false;
        }
    }
}