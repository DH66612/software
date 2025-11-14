package com.echo.dao;

import com.echo.entity.Category;
import com.echo.utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CategoryDaoImpl implements CategoryDao {
    @Override
    public Category findById(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Category category = null;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT * FROM categories WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                category = resultSetToCategory(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return category;
    }

    @Override
    public List<Category> findAll() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Category> categories = new ArrayList<>();

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT * FROM categories ORDER BY create_time DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                categories.add(resultSetToCategory(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return categories;
    }

    @Override
    public List<Category> findEnabledCategories() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Category> categories = new ArrayList<>();

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT * FROM categories WHERE status = 1 ORDER BY create_time DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                categories.add(resultSetToCategory(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return categories;
    }

    @Override


    public Category findByName(String name) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Category category = null;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT id, name, description, icon, color, sort_order, status, create_time, update_time FROM categories WHERE name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                category = resultSetToCategory(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询分类失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return category;
    }

    @Override
    public int insert(Category category) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int result = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "INSERT INTO categories (name, description, icon, color, sort_order, status, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, category.getName());
            pstmt.setString(2, category.getDescription());
            pstmt.setString(3, category.getIcon());
            pstmt.setString(4, category.getColor());
            pstmt.setInt(5, category.getSortOrder());
            pstmt.setInt(6, category.getStatus());
            pstmt.setTimestamp(7, new Timestamp(category.getCreateTime().getTime()));
            pstmt.setTimestamp(8, new Timestamp(category.getUpdateTime().getTime()));

            result = pstmt.executeUpdate();

            // 获取自增主键
            if (result > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    category.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("插入分类失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return result;
    }

    @Override
    public int update(Category category) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int result = 0;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "UPDATE categories SET name = ?, description = ?, icon = ?, color = ?, sort_order = ?, status = ?, update_time = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setString(3, category.getIcon());
            stmt.setString(4, category.getColor());
            stmt.setInt(5, category.getSortOrder());
            stmt.setInt(6, category.getStatus());
            stmt.setTimestamp(7, new Timestamp(category.getUpdateTime().getTime()));
            stmt.setInt(8, category.getId());

            result = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("更新分类失败", e);
        } finally {
            JdbcUtils.close(null, stmt, conn);
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
            String sql = "DELETE FROM categories WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("删除分类失败", e);
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
            String sql = "UPDATE categories SET status = ?, update_time = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, status);
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(3, id);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("更新分类状态失败", e);
        } finally {
            JdbcUtils.close(null, pstmt, conn);
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
            String sql = "SELECT COUNT(*) FROM categories";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("统计分类数量失败", e);
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return count;
    }
        @Override
        public List<Category> findByIds (List < Integer > ids) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            List<Category> categories = new ArrayList<>();

            if (ids == null || ids.isEmpty()) {
                return categories;
            }

            try {
                conn = JdbcUtils.getConnection();

                // 构建IN查询的参数占位符
                StringBuilder placeholders = new StringBuilder();
                for (int i = 0; i < ids.size(); i++) {
                    if (i > 0) placeholders.append(",");
                    placeholders.append("?");
                }

                String sql = "SELECT id, name, description, icon, color, sort_order, status, create_time, update_time FROM categories WHERE id IN (" + placeholders + ") ORDER BY sort_order ASC";
                pstmt = conn.prepareStatement(sql);

                // 设置参数
                for (int i = 0; i < ids.size(); i++) {
                    pstmt.setInt(i + 1, ids.get(i));
                }

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    Category category = resultSetToCategory(rs);
                    categories.add(category);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("查询分类列表失败", e);
            } finally {
                JdbcUtils.close(rs, pstmt, conn);
            }

            return categories;
        }
        @Override
        public List<Category> findPopularCategories ( int limit){
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            List<Category> categories = new ArrayList<>();

            try {
                conn = JdbcUtils.getConnection();
                String sql = "SELECT c.*, COUNT(ac.article_id) as article_count " +
                        "FROM categories c " +
                        "LEFT JOIN article_categories ac ON c.id = ac.category_id " +
                        "LEFT JOIN articles a ON ac.article_id = a.id AND a.status = 1 " +
                        "WHERE c.status = 1 " +
                        "GROUP BY c.id " +
                        "ORDER BY article_count DESC, c.sort_order ASC " +
                        "LIMIT ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, limit);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    Category category = resultSetToCategory(rs);
                    // 设置文章数量
                    category.setArticleCount(rs.getInt("article_count"));
                    categories.add(category);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("查询热门分类失败", e);
            } finally {
                JdbcUtils.close(rs, pstmt, conn);
            }

            return categories;
        }


        private Category resultSetToCategory(ResultSet rs) throws SQLException {
            Category category = new Category();

            // 设置基本字段
            category.setId(rs.getInt("id"));
            category.setName(rs.getString("name"));
            category.setDescription(rs.getString("description"));
            category.setIcon(rs.getString("icon"));
            category.setColor(rs.getString("color"));
            category.setSortOrder(rs.getInt("sort_order"));
            category.setStatus(rs.getInt("status"));

            // 处理时间字段
            Timestamp createTime = rs.getTimestamp("create_time");
            if (createTime != null) {
                category.setCreateTime(new Date(createTime.getTime()));
            }

            Timestamp updateTime = rs.getTimestamp("update_time");
            if (updateTime != null) {
                category.setUpdateTime(new Date(updateTime.getTime()));
            }

            return category;
        }
    @Override
    public int countArticleByCategoryId(Integer categoryId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = JdbcUtils.getConnection();
            // 修正SQL：通过关联表查询，并且只统计已发布的文章
            String sql = "SELECT COUNT(*) FROM articles a " +
                    "INNER JOIN article_categories ac ON a.id = ac.article_id " +
                    "WHERE ac.category_id = ? AND a.status = 1";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, categoryId);  // 添加这行设置参数
            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("统计分类文章数量失败", e);  // 修正错误信息
        } finally {
            JdbcUtils.close(rs, pstmt, conn);
        }

        return count;
    }

    }
