package com.echo.service;

import com.echo.entity.Comment;
import java.util.List;

public interface CommentService {
    Comment publishComment(Comment comment);
    List<Comment> getCommentsByArticleId(Integer articleId, int page, int pageSize);
    int getCommentCountByArticleId(Integer articleId);
    boolean deleteComment(Integer id);
    boolean updateComment(Comment comment);

}