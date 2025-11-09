package com.echo.dao;
import com.echo.entity.Article;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
public interface ArticleDao {
    Article findById(Integer Id);


    List<Article> findAll(int offset, int limit);

    List<Article> findByUserId(Integer userId, int offset, int limit);

    List<Article> findPublishedArticles(int offset, int limit);



    List<Article> findByCategoryId(Integer categoryId, int offset, int limit);

    List<Article> search(String keyword, int offset, int limit);

    int insert(Article article, Connection conn) throws SQLException;
    int addCategory(Integer articleId, Integer categoryId, Connection conn) throws SQLException;
    int update(Article article);
    int delete(Integer id);


    int updateStatus(Integer id, Integer status);

    int incrementViewCount(Integer id);

    int incrementLikeCount(Integer id);
    int decrementLikeCount(Integer id);
    int incrementCommentCount(Integer id);
    int decrementCommentCount(Integer id);
    int count();
    int countByCategoryId(Integer categoryId);
    int countByUserId(Integer userId);
    int countBySearch(String keyword);



    int deleteCategoriesByArticleId(Integer articleId);

    List<Integer> findCategoryIdsByArticleId(Integer articleId);
}
