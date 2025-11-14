//连接后端与数据库

package com.echo.dao;

import com.echo.entity.Article;
import com.echo.utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class ArticleDaoImpl implements ArticleDao {
// //resultset是jdbc的结果集,是数据库查询返回的数据集合
    private Article ResultSetToArticle(ResultSet rs) throws SQLException {
        Article article = new Article();
        article.setid(rs.getInt("id"));
        article.setauthorid(rs.getInt("author_id"));
        article.settitle(rs.getString("title"));
        article.setviewCount(rs.getInt("view_count"));
        article.setcontent(rs.getString("content"));
        article.setSummary(rs.getString("summary"));
        article.setHtmlContent(rs.getString("html_content"));
        article.setlikeCount(rs.getInt("like_count"));
        article.setcommentCount(rs.getInt("comment_count"));
        article.setstatus(rs.getInt("status"));

        Timestamp createTime = rs.getTimestamp("create_time");
        if (createTime != null) {
            article.setcreateTime(new Date(createTime.getTime()));
        }
        Timestamp updateTime = rs.getTimestamp("update_time");
        if (updateTime != null) {
            article.setupdateTime(new Date(updateTime.getTime()));
        }
        Timestamp publishTime = rs.getTimestamp("publish_time");
        if (publishTime != null) {
            article.setpublishTime(new Date(publishTime.getTime()));
        }

        return article;

    }

    @Override
    public Article findById(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Article article = null;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT a.*, u.username as author_name, u.nickname as author_nickname " +
                    "FROM articles a " +
                    "LEFT JOIN users u ON a.author_id = u.id " +
                    "WHERE a.id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                article = ResultSetToArticle(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询文章失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return article;
    }@Override
    public List<Article> findAll(int offset, int limit) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Article> articles = new ArrayList<>();

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT a.*, u.username as author_name, u.nickname as author_nickname " +
                    "FROM articles a " +
                    "LEFT JOIN users u ON a.author_id = u.id " +
                    "ORDER BY a.create_time DESC LIMIT ? OFFSET ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Article article = ResultSetToArticle(rs);
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询文章列表失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return articles;
    }


    @Override
    public List<Article> findByUserId(Integer userId, int offset, int limit) {
        System.out.println("=== 🗃️ ArticleDao.findByUserId ===");
        System.out.println("SQL查询 - userId: " + userId + ", offset: " + offset + ", limit: " + limit);

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Article> articles = new ArrayList<>();

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT * FROM articles WHERE author_id = ? ORDER BY create_time DESC LIMIT ? OFFSET ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            pstmt.setInt(3, offset);

            System.out.println("📋 执行SQL: " + sql);
            System.out.println("📋 参数: userId=" + userId + ", limit=" + limit + ", offset=" + offset);

            rs = pstmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                Article article = ResultSetToArticle(rs);
                articles.add(article);
                count++;
            }
            System.out.println("✅ 数据库查询结果: " + count + " 篇文章");
            return articles;

        } catch (SQLException e) {
            System.out.println("❌ 数据库查询异常: " + e.getMessage());
            e.printStackTrace();
            return articles;
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }
    }


    @Override
    public List<Article> findPublishedArticles(int offset, int limit) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Article> articles = new ArrayList<>();

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT a.*, u.username as author_name, u.nickname as author_nickname " +
                    "FROM articles a " +
                    "LEFT JOIN users u ON a.author_id = u.id " +
                    "WHERE a.status = 1 " +  // 1表示已发布
                    "ORDER BY a.publish_time DESC LIMIT ? OFFSET ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            rs = pstmt.executeQuery();
//遍历结果集填充文章列表
            while (rs.next()) {
                Article article = ResultSetToArticle(rs);
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询文章列表失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return articles;
    }


    @Override
    public List<Article> findByCategoryId(Integer categoryId, int offset, int limit) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Article> articles = new ArrayList<>();

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT a.*, u.username as author_name, u.nickname as author_nickname " +
                    "FROM articles a " +
                    "LEFT JOIN users u ON a.author_id = u.id " +
                    "INNER JOIN article_categories ac ON a.id = ac.article_id " +
                    "WHERE ac.category_id = ? AND a.status = 1 " +
                    "ORDER BY a.publish_time DESC LIMIT ? OFFSET ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, categoryId);
            pstmt.setInt(2, limit);
            pstmt.setInt(3, offset);
            rs = pstmt.executeQuery();
//遍历结果集填充文章列表
            while (rs.next()) {
                Article article = ResultSetToArticle(rs);
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询分类文章失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return articles;
    }

    @Override
    public List<Article> search(String keyword, int offset, int limit) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Article> articles = new ArrayList<>();

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT a.*, u.username as author_name, u.nickname as author_nickname " +
                    "FROM articles a " +
                    "LEFT JOIN users u ON a.author_id = u.id " +
                    "WHERE (a.title LIKE ? OR a.content LIKE ?) AND a.status = 1 " +
                    "ORDER BY a.publish_time DESC LIMIT ? OFFSET ?";
            pstmt = conn.prepareStatement(sql);

            String likeKeyword = "%" + keyword + "%";
            pstmt.setString(1, likeKeyword);
            pstmt.setString(2, likeKeyword);
            pstmt.setInt(3, limit);
            pstmt.setInt(4, offset);

            rs = pstmt.executeQuery();
//遍历结果集填充文章列表
            while (rs.next()) {
                Article article = ResultSetToArticle(rs);
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("搜索文章失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return articles;
    }

    @Override
    public int insert(Article article, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;//声明预编译语句对象
        ResultSet rs = null;//结果集变量
        int result = 0;

        try {
            // 使用传入的连接，而不是自己获取
            String sql = "INSERT INTO articles (title, content, html_content, summary, author_id, status, view_count, like_count, comment_count, create_time, update_time, publish_time) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";//这是插入文章数据的SQL语句
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);//预编译SQL并返回数据库自动生成的主键


            pstmt.setString(1, article.gettitle());//设置文章标题参数
            pstmt.setString(2, article.getcontent());
            pstmt.setString(3, article.getHtmlContent());

            pstmt.setString(4, article.getSummary());
            pstmt.setInt(5, article.getauthorid());
            pstmt.setInt(6, article.getstatus());
            pstmt.setInt(7, article.getviewCount());
            pstmt.setInt(8, article.getlikeCount());
            pstmt.setInt(9, article.getcommentCount());
            pstmt.setTimestamp(10, new Timestamp(article.getcreateTime().getTime()));
            pstmt.setTimestamp(11, new Timestamp(article.getupdateTime().getTime()));
            pstmt.setTimestamp(12, new Timestamp(article.getpublishTime().getTime()));




            result = pstmt.executeUpdate();//执行SQL更新操作

            // 获取自增主键
            if (result > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    article.setid(rs.getInt(1));//获取生成的文章ID
                }
            }
        } finally {
            // 只关闭Statement，不关闭Connection
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }

        return result;
    }

    @Override
    public int update(Article article) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = JdbcUtils.getConnection();

            String sql = "UPDATE articles SET title = ?, content = ?, html_content = ?, summary = ?, status = ?, update_time = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, article.gettitle());
            pstmt.setString(2, article.getcontent());
            pstmt.setString(3, article.getHtmlContent());
            // 设置摘要字段
            pstmt.setString(4, article.getSummary());
            pstmt.setInt(5, article.getstatus());
            pstmt.setTimestamp(6, new Timestamp(article.getupdateTime().getTime()));
            pstmt.setInt(7, article.getid());


            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("更新文章失败", e);
        } finally {
            JdbcUtils.close(null, pstmt, conn);
        }

        return result;
    }

    @Override
    public int delete(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "DELETE FROM articles WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("删除文章失败", e);
        } finally {
            JdbcUtils.close(null, pstmt, conn);//关闭数据库连接资源
        }

        return result;
    }

    @Override
    public int updateStatus(Integer id, Integer status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = JdbcUtils.getConnection();//获取数据库连接
            String sql = "UPDATE articles SET status = ?, update_time = ? WHERE id = ?";
            //设置数据库参数
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, status);
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(3, id);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("更新文章状态失败", e);
        } finally {
            JdbcUtils.close(null, pstmt, conn);//关闭数据库连接资源
        }

        return result;
    }

    @Override
    public int incrementViewCount(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "UPDATE articles SET view_count = view_count + 1 WHERE id = ?";
            pstmt = conn.prepareStatement(sql);//创建预编译语句对象
            pstmt.setInt(1, id);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("增加阅读量失败", e);
        } finally {
            JdbcUtils.close(null, pstmt, conn);//关闭数据库连接资源
        }

        return result;
    }

    @Override
    public int incrementLikeCount(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "UPDATE articles SET like_count = like_count + 1 WHERE id = ?";
            pstmt = conn.prepareStatement(sql);//创建预编译语句对象
            pstmt.setInt(1, id);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("增加点赞数失败", e);
        } finally {
            JdbcUtils.close(null, pstmt, conn);//关闭数据库连接资源
        }

        return result;
    }

    @Override
    public int decrementLikeCount(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "UPDATE articles SET like_count = GREATEST(like_count - 1, 0) WHERE id = ?";
            pstmt = conn.prepareStatement(sql);//创建预编译语句对象
            pstmt.setInt(1, id);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("减少点赞数失败", e);
        } finally {
            JdbcUtils.close(null, pstmt, conn);//关闭数据库连接资源
        }

        return result;
    }

    @Override
    public int incrementCommentCount(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "UPDATE articles SET comment_count = comment_count + 1 WHERE id = ?";
            pstmt = conn.prepareStatement(sql);//创建预编译语句对象
            pstmt.setInt(1, id);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("增加评论数失败", e);
        } finally {
            JdbcUtils.close(null, pstmt, conn);//关闭数据库连接资源
        }

        return result;
    }

    @Override
    public int decrementCommentCount(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "UPDATE articles SET comment_count = GREATEST(comment_count - 1, 0) WHERE id = ?";
            pstmt = conn.prepareStatement(sql);//创建预编译语句对象
            pstmt.setInt(1, id);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("减少评论数失败", e);
        } finally {
            JdbcUtils.close(null, pstmt, conn);//关闭数据库连接资源
        }

        return result;
    }

    @Override
    public int count() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT COUNT(*) FROM articles";
            pstmt = conn.prepareStatement(sql);//创建预编译语句对象
            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("统计文章数量失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);//关闭数据库连接资源
        }

        return count;
    }

    @Override
    public int countByUserId(Integer userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT COUNT(*) FROM articles WHERE author_id = ?";
            pstmt = conn.prepareStatement(sql);//创建预编译语句对象
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("统计用户文章数量失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return count;
    }

    @Override
    public int countByCategoryId(Integer categoryId) {
        Connection conn = null;//声明数据库连接变量
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT COUNT(*) FROM articles a " +
                    "INNER JOIN article_categories ac ON a.id = ac.article_id " +
                    "WHERE ac.category_id = ? AND a.status = 1";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, categoryId);
            rs = pstmt.executeQuery();//执行SQL查询操作

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("统计分类文章数量失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return count;
    }

    @Override
    public int countBySearch(String keyword) {
        Connection conn = null;//声明数据库连接变量
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT COUNT(*) FROM articles WHERE (title LIKE ? OR content LIKE ?) AND status = 1";
            pstmt = conn.prepareStatement(sql);

            String likeKeyword = "%" + keyword + "%";
            pstmt.setString(1, likeKeyword);
            pstmt.setString(2, likeKeyword);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("统计搜索结果失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return count;
    }

    @Override
    public int addCategory(Integer articleId, Integer categoryId, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            String sql = "INSERT INTO article_categories (article_id, category_id) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, articleId);
            pstmt.setInt(2, categoryId);

            result = pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
        }

        return result;
    }

    @Override
    public int deleteCategoriesByArticleId(Integer articleId) {
        Connection conn = null;//声明数据库连接变量
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "DELETE FROM article_categories WHERE article_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, articleId);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("删除文章分类关联失败", e);
        } finally {
            JdbcUtils.close(null, pstmt, conn);
        }

        return result;
    }

    @Override
    public List<Integer> findCategoryIdsByArticleId(Integer articleId) {
        Connection conn = null;//声明数据库连接变量
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Integer> categoryIds = new ArrayList<>();

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT category_id FROM article_categories WHERE article_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, articleId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                categoryIds.add(rs.getInt("category_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询文章分类ID失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return categoryIds;
    }
}



