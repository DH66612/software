package com.echo.service;

import com.echo.dao.CategoryDao;
import com.echo.dao.CategoryDaoImpl;
import com.echo.entity.Category;

import java.util.Date;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao;

    public CategoryServiceImpl() {
        this.categoryDao = new CategoryDaoImpl(); // 假设有CategoryDaoImpl
    }

    // 也可以通过setter注入
    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryDao.findAll();
    }

    @Override
    public List<Category> getEnabledCategories() {
        return categoryDao.findEnabledCategories();
    }

    @Override
    public Category getCategoryById(Integer id) {
        if (id == null) {
            return null;
        }

        return categoryDao.findById(id);
    }

    @Override
    public Category getCategoryByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        return categoryDao.findByName(name.trim());
    }

    @Override
    public Category createCategory(Category category) throws RuntimeException {
        System.out.println("开始创建分类: " + category.getName());

        // 1. 数据验证
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new RuntimeException("分类名称不能为空");
        }

        // 2. 检查分类名称是否已存在
        if (isCategoryNameExists(category.getName())) {
            throw new RuntimeException("分类名称已存在");
        }

        // 3. 设置默认值
        if (category.getDescription() == null) {
            category.setDescription("");
        }

        if (category.getIcon() == null) {
            category.setIcon("📁"); // 默认图标
        }

        if (category.getColor() == null) {
            category.setColor("#666666"); // 默认颜色
        }

        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }

        if (category.getStatus() == null) {
            category.setStatus(1); // 默认启用
        }

        // 设置时间
        Date now = new Date();
        category.setCreateTime(now);
        category.setUpdateTime(now);

        // 4. 保存分类
        try {
            int result = categoryDao.insert(category);
            if (result <= 0) {
                throw new RuntimeException("分类创建失败");
            }

            System.out.println("分类创建成功，ID: " + category.getId());
            return category;

        } catch (Exception e) {
            throw new RuntimeException("分类创建失败: " + e.getMessage());
        }
    }

    @Override
    public Category updateCategory(Category category) throws RuntimeException {
        if (category == null || category.getId() == null) {
            throw new RuntimeException("分类信息不完整");
        }

        // 1. 检查分类是否存在
        Category existingCategory = categoryDao.findById(category.getId());
        if (existingCategory == null) {
            throw new RuntimeException("分类不存在");
        }

        // 2. 数据验证
        if (category.getName() != null && category.getName().trim().isEmpty()) {
            throw new RuntimeException("分类名称不能为空");
        }


        // 4. 更新字段
        if (category.getName() != null) {
            existingCategory.setName(category.getName());
        }

        if (category.getDescription() != null) {
            existingCategory.setDescription(category.getDescription());
        }

        if (category.getIcon() != null) {
            existingCategory.setIcon(category.getIcon());
        }

        if (category.getColor() != null) {
            existingCategory.setColor(category.getColor());
        }

        if (category.getSortOrder() != null) {
            existingCategory.setSortOrder(category.getSortOrder());
        }

        if (category.getStatus() != null) {
            existingCategory.setStatus(category.getStatus());
        }

        existingCategory.setUpdateTime(new Date());
        // 5. 更新分类
        try {
            int result = categoryDao.update(existingCategory);
            if (result <= 0) {
                throw new RuntimeException("分类更新失败");
            }

            System.out.println("分类更新成功，ID: " + category.getId());
            return existingCategory;

        } catch (Exception e) {
            throw new RuntimeException("分类更新失败: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteCategory(Integer id) throws RuntimeException {
        if (id == null) {
            throw new RuntimeException("分类ID不能为空");
        }

        // 检查分类是否存在

        Category category = categoryDao.findById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }

        // 检查分类下是否有文章
        int articleCount = getArticleCountByCategory(id);
        if (articleCount > 0) {
            throw new RuntimeException("该分类下还有 " + articleCount + " 篇文章，无法删除");
        }

        try {
            int result = categoryDao.delete(id);
            return result > 0;

        } catch (Exception e) {
            throw new RuntimeException("分类删除失败: " + e.getMessage());
        }
    }

    @Override
    public boolean enableCategory(Integer id, Integer status) throws RuntimeException {
        if (id == null) {
            throw new RuntimeException("分类ID不能为空");
        }

        Category category = categoryDao.findById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }

        if (category.isEnabled()) {
            throw new RuntimeException("分类已是启用状态");
        }

        category.setStatus(1); // 设置为启用状态
        category.setUpdateTime(new Date());

        try {
            int result = categoryDao.updateStatus(id, status);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("启用分类失败: " + e.getMessage());
        }
    }

    @Override
    public boolean disableCategory(Integer id, Integer status) throws RuntimeException {
        if (id == null) {
            throw new RuntimeException("分类ID不能为空");
        }

        Category category = categoryDao.findById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        new Date();

        try {
            int result = categoryDao.updateStatus(id, status);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("禁用分类失败: " + e.getMessage());
        }
    }

    @Override
    public int getArticleCountByCategory(Integer categoryId) {
        if (categoryId == null) {
            return 0;
        }

        return categoryDao.countArticleByCategoryId(categoryId);
    }

    @Override
    public List<Category> getPopularCategories(int limit) {
        if (limit < 1) {
            limit = 10; // 默认返回10个
        }

        return categoryDao.findPopularCategories(limit);
    }

    @Override
    public boolean isCategoryNameExists(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        Category category = categoryDao.findByName(name.trim());
        return category != null;
    }

    @Override
    public boolean isCategoryNameExists(String name, Integer excludeId) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        Category category = categoryDao.findByName(name.trim());
        return category != null && !category.getId().equals(excludeId);
    }

    @Override
    public boolean saveCategory(Category category) {
        try {
            int result = categoryDao.insert(category);
            if (result <= 0) {
                throw new RuntimeException("分类创建失败");
            }

            System.out.println("分类创建成功，ID: " + category.getId());
            return true;

        } catch (Exception e) {
            throw new RuntimeException("分类创建失败: " + e.getMessage());
        }

    }

    @Override
    public int getCategoryCount() {
        return categoryDao.count();
    }
}