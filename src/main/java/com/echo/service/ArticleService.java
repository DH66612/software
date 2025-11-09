package com.echo.service;
import com.echo.entity.Article;
import java.util.List;
public interface ArticleService {


    Article publishArticle(Article article, int[] categoryIds) throws RuntimeException;

    Article getArticleById(Integer id);
    List<Article> getPublishArticles(int page,int pageSize);







    List<Article> getArticlesByUserid(Integer userId, int page, int pageSize);


    List<Article> getArticlesByCategoryid(Integer categoryId, int page, int pageSize);

    List<Article> searchArticles(String keyword, int page, int pageSize);
    Article updateArticle(Article article,int[]categoryies);
    boolean deleteArticle(Integer id);

    // 将这个方法重命名，避免与搜索方法冲突
    List<Article> getAllArticles(int page, int pageSize);

    boolean incrementViewCount(Integer id);

    boolean likeArticle(Integer id);

    boolean unlikeArticle(Integer id);



    boolean incrementCommentCount(Integer id);

    boolean decrementCommentCount(Integer id);

    int getArticleCount();





    int getArticleCountByUserId(Integer userId);

    int getArticleCountByCategoryId(Integer categoryId);

    int getSearchCount(String keyword);

    List<Article> searchArticles(int page, int pageSize);
}
