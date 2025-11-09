package com.echo.dao;

import com.echo.entity.Comment;
import com.echo.utils.JdbcUtils;
import java.sql.*;

public class SimpleCommentDao {

    public boolean testInsert() {
        String sql = "INSERT INTO comments (article_id, user_id, content, status) VALUES (1, 1, '测试评论', 1)";

        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int result = pstmt.executeUpdate();
            System.out.println("插入结果: " + result);
            return result > 0;

        } catch (SQLException e) {
            System.out.println("插入失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void testSelect() {
        String sql = "SELECT * FROM comments LIMIT 5";

        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("查询结果:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", 内容: " + rs.getString("content"));
            }

        } catch (SQLException e) {
            System.out.println("查询失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SimpleCommentDao dao = new SimpleCommentDao();
        System.out.println("=== 开始测试 ===");
        dao.testInsert();
        dao.testSelect();
        System.out.println("=== 测试结束 ===");
    }
}