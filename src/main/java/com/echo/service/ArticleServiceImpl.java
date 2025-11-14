package com.echo.service;

import com.echo.dao.ArticleDao;
import com.echo.dao.CategoryDao;
import com.echo.entity.Article;
import com.echo.utils.JdbcUtils;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.echo.dao.ArticleDaoImpl;
import com.echo.dao.CategoryDaoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ArticleServiceImpl implements ArticleService {

    private ArticleDao articleDao;
    private CategoryDao categoryDao;

    // Markdown 解析器
    private Parser markdownParser;
    private HtmlRenderer htmlRenderer;//声明HTML渲染器变量,用于在后端生成HTML字符串

    public ArticleServiceImpl() {
        this.articleDao = new ArticleDaoImpl(); // 假设有ArticleDaoImpl
        this.categoryDao = new CategoryDaoImpl(); // 假设有CategoryDaoImpl

        // 初始化Markdown解析器
        this.markdownParser = Parser.builder().build();
        this.htmlRenderer = HtmlRenderer.builder().build();
    }

    // 也可以通过setter注入
    public void setArticleDao(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public Article publishArticle(Article article, int[] categoryIds) throws RuntimeException {
        Connection conn = null;
        System.out.println("=== DEBUG: 开始发布文章 ===");
        System.out.println("DEBUG: 文章标题: " + article.gettitle());
        System.out.println("DEBUG: 作者ID: " + article.getauthorid());
        System.out.println("DEBUG: 内容长度: " + (article.getcontent() != null ? article.getcontent().length() : 0));

        System.out.println("=== 开始发布文章 ===");
        System.out.println("文章标题: " + article.gettitle());
        System.out.println("作者ID: " + article.getauthorid());
        if (article.gettitle() == null || article.gettitle().trim().isEmpty()) {
            throw new RuntimeException("文章标题不能为空");
        }
//检查文章内容是否为空
        if (article.getcontent() == null || article.getcontent().trim().isEmpty()) {
            throw new RuntimeException("文章内容不能为空");
        }
//检查作者ID是否为空
        if (article.getauthorid() == null) {
            throw new RuntimeException("作者ID不能为空");
        }

        // 2. 设置默认值
        if (article.getstatus() == null) {
            article.setstatus(1); // 默认已发布
        }

        if (article.getviewCount() == null) {
            article.setviewCount(0);
        }

        if (article.getlikeCount() == null) {
            article.setlikeCount(0);
        }

        if (article.getcommentCount() == null) {
            article.setcommentCount(0);
        }

        // 如果摘要为空，可以从内容中自动生成摘要（前100个字符）
        if (article.getSummary() == null || article.getSummary().trim().isEmpty()) {
            String content = article.getcontent();
            if (content.length() > 100) {
                article.setSummary(content.substring(0, 100) + "...");
            } else {
                article.setSummary(content);
            }
        }

        // 设置时间
        Date now = new Date();
        if (article.getcreateTime() == null) {
            article.setcreateTime(now);
        }
        article.setupdateTime(now);
        if (article.getpublishTime() == null) {
            article.setpublishTime(now);
        }

        // 3. 将Markdown转换为HTML
        if (article.getcontent() != null && !article.getcontent().isEmpty()) {
            try {
                Node document = markdownParser.parse(article.getcontent());//解析Markdown内容为文档对象
                String htmlContent = htmlRenderer.render(document);//将文档渲染为HTML
                article.setHtmlContent(htmlContent);//设置文章HTML内容
            } catch (Exception e) {
                throw new RuntimeException("内容格式转换失败: " + e.getMessage());
            }
        }


        article.setupdateTime(new Date());


        // 4. 保存文章
        try {
            conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false); // 关闭自动提交，开始事务

            System.out.println("获取数据库连接成功，开始事务");
            int result = articleDao.insert(article, conn);//调用DAO插入文章数据
            System.out.println("开始插入文章到数据库...");

            System.out.println("文章插入结果: " + result);
            System.out.println("DEBUG: 准备调用 articleDao.insert...");
           ;
            System.out.println("DEBUG: articleDao.insert 返回: " + result);
            System.out.println("DEBUG: 文章ID: " + article.getid());

            if (result <= 0) {//检查更新是否失败
                throw new RuntimeException("文章保存失败");
            }
            System.out.println("文章插入成功，生成ID: " + article.getid());
            // 5. 保存分类关联
            if (categoryIds != null && categoryIds.length > 0) {
                for (int categoryId : categoryIds) {
                    articleDao.addCategory(article.getid(), categoryId, conn);
                }
            }

            System.out.println("文章发布成功，ID: " + article.getid());
            conn.commit();
            return article;

        } catch (Exception e) {   if (conn != null) {
            try {
                System.out.println("DEBUG: 发布过程中异常: " + e.getMessage());
                conn.rollback();//回滚数据库事务
                System.out.println("事务回滚！");
            } catch (SQLException ex) {
                ex.printStackTrace();

            }
        }
            System.out.println("文章发布过程中出现异常: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("文章发布失败: " + e.getMessage());
        } finally {
            // 恢复自动提交并关闭连接
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {//捕获数据库异常
                    e.printStackTrace();
                }
            }
        }


    }


    @Override
    public Article getArticleById(Integer id) {
        if (id == null) {
            return null;
        }

        Article article = articleDao.findById(id);
        if (article != null && article.isPublished()) {
            // 可以在这里加载分类信息
            List<Integer> categoryIds = articleDao.findCategoryIdsByArticleId(id);
            // 如果需要完整的分类信息，可以在这里加载
        }

        return article;
    }

    @Override
    public List<Article> getPublishArticles(int page, int pageSize) {
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;

        int offset = (page - 1) * pageSize;
        return articleDao.findPublishedArticles(offset, pageSize);
    }
    @Override
    public List<Article> searchArticles(int page, int pageSize){
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;

        int offset = (page - 1) * pageSize;
        return articleDao.findAll(offset, pageSize);
    }
    @Override

    public List<Article> getArticlesByUserid(Integer userId, int page, int pageSize) {
        System.out.println("ArticleService - 获取用户文章: userId=" + userId + ", page=" + page + ", pageSize=" + pageSize);

        if (userId == null) {
            System.out.println("用户ID为空，返回空列表");
            return new ArrayList<>();
        }

        int offset = (page - 1) * pageSize;
        List<Article> articles = articleDao.findByUserId(userId, offset, pageSize);
        System.out.println("从DAO获取到的文章数量: " + articles.size());

        return articles;
    }




    @Override
    public List<Article> getArticlesByCategoryid(Integer categoryId, int page, int pageSize) {
        if (categoryId == null) {
            throw new RuntimeException("分类ID不能为空");
        }

        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;

        int offset = (page - 1) * pageSize;
        return articleDao.findByCategoryId(categoryId, offset, pageSize);
    }

    @Override
    public List<Article> searchArticles(String keyword, int page, int pageSize) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getPublishArticles(page, pageSize);
        }

        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;

        int offset = (page - 1) * pageSize;
        return articleDao.search(keyword.trim(), offset, pageSize);
    }

    @Override
    public Article updateArticle(Article article, int[] categoryIds) throws RuntimeException {
        Connection conn = null;
        if (article == null || article.getid() == null) {
            throw new RuntimeException("文章信息不完整");
        }
        // 1. 检查文章是否存在
        Article existingArticle = articleDao.findById(article.getid());
        if (existingArticle == null) {
            throw new RuntimeException("文章不存在");
        }

        // 2. 数据验证
        if (article.gettitle() != null && article.gettitle().trim().isEmpty()) {
            throw new RuntimeException("文章标题不能为空");
        }

        // 3. 更新字段
        if (article.gettitle() != null) {
            existingArticle.settitle(article.gettitle());
        }
        // 更新摘要字段
        if (article.getSummary() != null) {
            article.setSummary(article.getSummary());
        }

        if (article.getstatus() != null) {
            article.setstatus(article.getstatus());
        }
        if (article.getcontent() != null) {
            existingArticle.setcontent(article.getcontent());

            // 重新生成HTML内容
            try {
                Node document = markdownParser.parse(article.getcontent());
                String htmlContent = htmlRenderer.render(document);
                existingArticle.setHtmlContent(htmlContent);
            } catch (Exception e) {
                throw new RuntimeException("内容格式转换失败: " + e.getMessage());
            }
        }

        if (article.getstatus() != null) {
            existingArticle.setstatus(article.getstatus());
        }

        

        existingArticle.setupdateTime(new Date());

        // 4. 更新文章
        try {
            //获取连接并开始事务
                    conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false); // 关闭自动提交，开始事务

            System.out.println("获取数据库连接成功，开始事务");

            int result = articleDao.update(existingArticle);
            if (result <= 0) {
                throw new RuntimeException("文章更新失败");
            }

            // 5. 更新分类关联
            if (categoryIds != null) {
                // 先删除原有的分类关联
                articleDao.deleteCategoriesByArticleId(article.getid());

                // 添加新的分类关联
                for (int categoryId : categoryIds) {
                    articleDao.addCategory(article.getid(), categoryId,conn);
                }
            }

            System.out.println("文章更新成功，ID: " + article.getid());
            conn.commit();
            return existingArticle;

        } catch (Exception e) {
            throw new RuntimeException("文章更新失败: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteArticle(Integer id) throws RuntimeException {
        if (id == null) {
            throw new RuntimeException("文章ID不能为空");
        }

        // 检查文章是否存在
        Article article = articleDao.findById(id);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }

        try {
            // 先删除分类关联
            articleDao.deleteCategoriesByArticleId(id);

            // 再删除文章
            int result = articleDao.delete(id);
            return result > 0;

        } catch (Exception e) {
            throw new RuntimeException("文章删除失败: " + e.getMessage());
        }
    }
    // 将这个方法重命名，避免与搜索方法冲突
    @Override
    public List<Article> getAllArticles(int page, int pageSize) {
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;

        int offset = (page - 1) * pageSize;
        return articleDao.findAll(offset, pageSize);
    }
    @Override
    public boolean incrementViewCount(Integer id) {
        if (id == null) {
            return false;
        }

        try {
            int result = articleDao.incrementViewCount(id);
            return result > 0;
        } catch (Exception e) {
            System.err.println("增加阅读量失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean likeArticle(Integer id) {
        if (id == null) {
            return false;
        }

        try {
            int result = articleDao.incrementLikeCount(id);
            return result > 0;
        } catch (Exception e) {
            System.err.println("点赞失败: " + e.getMessage());
            return false;
        }
    }@Override
    public boolean unlikeArticle(Integer id) {
        if (id == null) {
            return false;
        }

        try {
            int result = articleDao.decrementLikeCount(id);
            return result > 0;
        } catch (Exception e) {
            System.err.println("取消点赞失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean incrementCommentCount(Integer id) {
        if (id == null) {
            return false;
        }

        try {
            int result = articleDao.incrementCommentCount(id);
            return result > 0;
        } catch (Exception e) {
            System.err.println("增加评论数失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean decrementCommentCount(Integer id) {
        if (id == null) {
            return false;
        }

        try {
            int result = articleDao.decrementCommentCount(id);
            return result > 0;
        } catch (Exception e) {
            System.err.println("减少评论数失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int getArticleCount() {
        return articleDao.count();
    }

    @Override
    public int getArticleCountByUserId(Integer userId) {
        if (userId == null) {
            return 0;
        }

        return articleDao.countByUserId(userId);
    }

    @Override
    public int getArticleCountByCategoryId(Integer categoryId) {
        if (categoryId == null) {
            return 0;
        }

        return articleDao.countByCategoryId(categoryId);
    }

    @Override
    public int getSearchCount(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return articleDao.count();
        }

        return articleDao.countBySearch(keyword.trim());
    }
}