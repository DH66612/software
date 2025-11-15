package com.echo.service;
import com.echo.entity.Article;
import com.echo.entity.Category;

import javax.servlet.ServletException;
import java.util.List;
public interface CategoryService {
    List<Category> getAllCategories();
    List<Category> getEnabledCategories();
    Category getCategoryById(Integer Id);


    Category getCategoryByName(String name);

    Category createCategory(Category category)throws RuntimeException;
    Category updateCategory(Category category)throws RuntimeException;
    boolean deleteCategory(Integer Id)throws RuntimeException;
    boolean enableCategory(Integer Id,Integer status)throws RuntimeException;
    boolean disableCategory(Integer Id,Integer status)throws RuntimeException;
    int getArticleCountByCategory(Integer id);

    List<Category> getCategoriesByArticleId(Integer articleId);

    List<Category> getPopularCategories(int limit);

    boolean isCategoryNameExists(String name);

    boolean isCategoryNameExists(String name, Integer excludeId);

    int getCategoryCount();


    boolean saveCategory(Category category);
}
