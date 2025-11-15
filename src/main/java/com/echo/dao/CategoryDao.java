//怎么改也不能把分类做好（哭）
package com.echo.dao;
import com.echo.entity.Category;
import java.util.List;
public interface CategoryDao {
    Category findById(Integer id);

    List<Category> findByArticleId(Integer articleId);

    List<Category> findAll();

    List<Category> findEnabledCategories();


    Category findByName(String name);

    Category findByName(String name, Integer excludeId);

    int insert(Category category);

    int delete(Integer id);

    int update(Category category);

    int updateStatus(Integer id, Integer status);

    int count();

    List<Category> findByIds(List<Integer> ids);

    List<Category> findPopularCategories(int limit);

    int countArticleByCategoryId(Integer categoryId);

}
