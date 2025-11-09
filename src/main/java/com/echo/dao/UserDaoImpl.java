package com.echo.dao;

import com.echo.entity.User;
import com.echo.utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    //resultset是jdbc的结果集
    //resultsettouser将 ResultSet 转换为 User 对象,用于从数据库结果集创建User对象
    @Override
    public boolean updateAvatar(Integer userId, String avatarUrl) {
        String sql = "UPDATE users SET avatar = ?, update_time = NOW() WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JdbcUtils.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, avatarUrl);
            pstmt.setInt(2, userId);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(null, pstmt, conn);
        }
        return false;
    }
    private User resultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();

        // 设置基本字段
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setNickname(rs.getString("nickname"));
        user.setRole(rs.getInt("role"));
        user.setStatus(rs.getInt("status"));
        user.setAvatar(rs.getString("avatar"));

        // 处理时间字段（可能为null）
        Timestamp createTime = rs.getTimestamp("create_time");
        if (createTime != null) {
            user.setCreateTime(new Date(createTime.getTime()));
        }

        Timestamp updateTime = rs.getTimestamp("update_time");
        if (updateTime != null) {
            user.setUpdateTime(new Date(updateTime.getTime()));
        }

        return user;
    }
    @Override
    public User findById(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT id, username, password, email, nickname, role, status, avatar, create_time, update_time FROM users WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = resultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询用户失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return user;
    }


    @Override
    public User findByUsername(String username) {
        System.out.println("开始查询用户: " + username);

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = JdbcUtils.getConnection();
            System.out.println("数据库连接获取成功");

            String sql = "SELECT id, username, password, email, nickname, role, status, avatar, create_time, update_time FROM users WHERE username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            System.out.println("执行SQL查询: " + sql + "，参数: " + username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("找到用户记录");
                user = resultSetToUser(rs);
            } else {
                System.out.println("未找到用户记录 - 这是正常的，说明用户名可用");
            }
        } catch (SQLException e) {
            System.err.println("查询用户失败 - SQL异常: " + e.getMessage());
            System.err.println("SQL状态: " + e.getSQLState());
            System.err.println("错误代码: " + e.getErrorCode());
            e.printStackTrace();
            throw new RuntimeException("查询用户失败", e);
        } finally {
            JdbcUtils.close(rs, stmt, conn);
        }

        return user;
    }

    @Override
    public User findByEmail(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT id, username, password, email, nickname, role, status, avatar, create_time, update_time FROM users WHERE email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = resultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询用户失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return user;
    }

    @Override
    public int insert(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int result = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "INSERT INTO users (username, password, email, nickname, role, status, avatar, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getNickname());
            pstmt.setInt(5, user.getRole());
            pstmt.setInt(6, user.getStatus());
            pstmt.setString(7, user.getAvatar());
            pstmt.setTimestamp(8, new Timestamp(user.getCreateTime().getTime()));
            pstmt.setTimestamp(9, new Timestamp(user.getUpdateTime().getTime()));

            result = pstmt.executeUpdate();

            // 获取自增主键
            if (result > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("插入用户失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return result;
    }

    @Override
    public int update(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "UPDATE users SET username = ?, email = ?, nickname = ?, role = ?, status = ?, avatar = ?, update_time = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getNickname());
            pstmt.setInt(4, user.getRole());
            pstmt.setInt(5, user.getStatus());
            pstmt.setString(6, user.getAvatar());
            pstmt.setTimestamp(7, new Timestamp(user.getUpdateTime().getTime()));
            pstmt.setInt(8, user.getId());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("更新用户失败", e);
        } finally {
            JdbcUtils.close(null, pstmt, conn);
        }

        return result;
    }

    @Override
    public int updatePassword(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "UPDATE users SET password = ?, update_time = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user.getPassword());
            pstmt.setTimestamp(2, new Timestamp(user.getUpdateTime().getTime()));
            pstmt.setInt(3, user.getId());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("更新密码失败", e);
        } finally {
            JdbcUtils.close(null, pstmt, conn);
        }

        return result;
    }

    @Override
    public int updateStatus(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "UPDATE users SET status = ?, update_time = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, user.getStatus());
            pstmt.setTimestamp(2, new Timestamp(user.getUpdateTime().getTime()));
            pstmt.setInt(3, user.getId());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("更新用户状态失败", e);
        } finally {
            JdbcUtils.close(null, pstmt, conn);
        }

        return result;
    }

    @Override
    public List<User> findAll(int offset, int limit) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT id, username, password, email, nickname, role, status, avatar, create_time, update_time FROM users ORDER BY create_time DESC LIMIT ? OFFSET ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = resultSetToUser(rs);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询用户列表失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return users;
    }

    @Override
    public int count() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT COUNT(*) FROM users";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("统计用户数量失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return count;
    }

    @Override
    public List<User> search(String keyword, int offset, int limit) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT id, username, password, email, nickname, role, status, avatar, create_time, update_time FROM users WHERE username LIKE ? OR email LIKE ? OR nickname LIKE ? ORDER BY create_time DESC LIMIT ? OFFSET ?";      pstmt = conn.prepareStatement(sql);

            String likeKeyword = "%" + keyword + "%";
            pstmt.setString(1, likeKeyword);
            pstmt.setString(2, likeKeyword);
            pstmt.setString(3, likeKeyword);
            pstmt.setInt(4, limit);
            pstmt.setInt(5, offset);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = resultSetToUser(rs);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("搜索用户失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return users;
    }
}