package com.echo.dao;

import com.echo.entity.Comment;
import com.echo.utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDaoImpl implements CommentDao {
//数据库查询返回的结果集，我们要在这里将它转换为字符串与整型
//resultset是jdbc的结果集
    private Comment resultSetToComment(ResultSet rs) throws SQLException {
        Comment comment = new Comment();
        comment.setId(rs.getInt("id"));
        comment.setArticleId(rs.getInt("article_id"));
        comment.setUserId(rs.getInt("user_id"));
        comment.setContent(rs.getString("content"));
        comment.setStatus(rs.getInt("status"));

        Timestamp createTime = rs.getTimestamp("create_time");
        if (createTime != null) {
            comment.setCreateTime(new Date(createTime.getTime()));
        }

        Timestamp updateTime = rs.getTimestamp("update_time");
        if (updateTime != null) {
            comment.setUpdateTime(new Date(updateTime.getTime()));
        }

        return comment;
    }
//在数据库中插入评论
    @Override
    public int insert(Comment comment) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int result = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "INSERT INTO comments (article_id, user_id, content, status, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//因为IDEA是静态解析的所以不能解析这个表，不过运行起来没事
            pstmt.setInt(1, comment.getArticleId());
            pstmt.setInt(2, comment.getUserId());
            pstmt.setString(3, comment.getContent());
            pstmt.setInt(4, comment.getStatus());
            pstmt.setTimestamp(5, new Timestamp(comment.getCreateTime().getTime()));
            pstmt.setTimestamp(6, new Timestamp(comment.getUpdateTime().getTime()));

            result = pstmt.executeUpdate();

            // 获取自增主键
            if (result > 0) {
                rs = pstmt.getGeneratedKeys();//获取自动生成的主键
                if (rs.next()) {
                    comment.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("插入评论失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return result;
    }
//通过文章ID来寻找它相对应的评论
@Override
public List<Comment> findByArticleId(Integer articleId, int offset, int limit) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<Comment> comments = new ArrayList<>();

    try {
        conn = JdbcUtils.getConnection();
        String sql = "SELECT c.*, u.username as author_name, u.nickname as author_nickname, u.avatar as author_avatar " +
                "FROM comments c " +
                "LEFT JOIN users u ON c.user_id = u.id " +
                "WHERE c.article_id = ? AND c.status = 1 " +
                "ORDER BY c.create_time DESC LIMIT ? OFFSET ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, articleId);
        pstmt.setInt(2, limit);
        pstmt.setInt(3, offset);

        rs = pstmt.executeQuery();

        while (rs.next()) {
            Comment comment = resultSetToComment(rs);

            // 设置作者信息 - 修复逻辑
            String nickname = rs.getString("author_nickname");
            String username = rs.getString("author_name");
            String avatar = rs.getString("author_avatar");

            // 优先使用昵称，如果昵称为空则使用用户名
            if (nickname != null && !nickname.trim().isEmpty()) {
                comment.setAuthorName(nickname.trim());
            } else if (username != null && !username.trim().isEmpty()) {
                comment.setAuthorName(username.trim());
            } else {
                comment.setAuthorName("匿名用户");
            }

            // 设置头像，如果为空则使用默认头像
            if (avatar != null && !avatar.trim().isEmpty()) {
                comment.setAuthorAvatar(avatar.trim());
            } else {
                comment.setAuthorAvatar("/images/avatar-default.png");
            }

            // 调试信息
            System.out.println("评论ID: " + comment.getId() +
                    ", 用户ID: " + comment.getUserId() +
                    ", 昵称: " + nickname +
                    ", 用户名: " + username +
                    ", 最终显示: " + comment.getAuthorName());

            comments.add(comment);
        }

        System.out.println("查询到 " + comments.size() + " 条评论");
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("查询评论失败", e);
    } finally {
        JdbcUtils.close(rs, pstmt, conn);
    }

    return comments;
}
//通过文章id来
    @Override
    public int countByArticleId(Integer articleId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT COUNT(*) FROM comments WHERE article_id = ? AND status = 1";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, articleId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("统计评论数量失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return count;
    }

    @Override

    public Comment findById(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Comment comment = null;

        try {
            conn = JdbcUtils.getConnection(); // 修复这里的语法错误
            String sql = "SELECT c.*, u.username as author_name, u.nickname as author_nickname, u.avatar as author_avatar " +
                    "FROM comments c " +
                    "LEFT JOIN users u ON c.user_id = u.id " +
                    "WHERE c.id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                comment = resultSetToComment(rs);

                // 设置作者信息
                String nickname = rs.getString("author_nickname");
                String username = rs.getString("author_name");
                String avatar = rs.getString("author_avatar");

                if (nickname != null && !nickname.trim().isEmpty()) {
                    comment.setAuthorName(nickname.trim());
                } else if (username != null && !username.trim().isEmpty()) {
                    comment.setAuthorName(username.trim());
                } else {
                    comment.setAuthorName("匿名用户");
                }

                if (avatar != null && !avatar.trim().isEmpty()) {
                    comment.setAuthorAvatar(avatar.trim());
                } else {
                    comment.setAuthorAvatar("/images/avatar-default.png");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询评论失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return comment;
    }
    @Override
    public int update(Comment comment) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "UPDATE comments SET content = ?, status = ?, update_time = ? WHERE id = ?";//?是参数占位符
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, comment.getContent());
            pstmt.setInt(2, comment.getStatus());
            pstmt.setTimestamp(3, new Timestamp(comment.getUpdateTime().getTime()));
            pstmt.setInt(4, comment.getId());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("更新评论失败", e);
        } finally {
            JdbcUtils.close(null, pstmt, conn);//只关闭连接与预编译语句
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
            String sql = "DELETE FROM comments WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);//SQL参数位置索引

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("删除评论失败", e);
        } finally {
            JdbcUtils.close(null, pstmt, conn);
        }

        return result;
    }

    @Override
    public int updateStatus(Integer id, Integer status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "UPDATE comments SET status = ?, update_time = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, status);
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(3, id);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("更新评论状态失败", e);
        } finally {
            JdbcUtils.close(null, pstmt, conn);
        }

        return result;
    }
}