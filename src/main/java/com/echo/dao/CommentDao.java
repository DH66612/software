package com.echo.dao;

import com.echo.entity.Comment;
import java.util.List;

public interface CommentDao {
    int insert(Comment comment);
    List<Comment> findByArticleId(Integer articleId, int offset, int limit);
    int countByArticleId(Integer articleId);
    Comment findById(Integer id);
    int update(Comment comment);
    int delete(Integer id);
    int updateStatus(Integer id, Integer status);
}