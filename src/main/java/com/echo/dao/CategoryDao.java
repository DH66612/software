//怎么改也不能把分类做好（哭）
package com.echo.dao;
import com.echo.entity.Category;
import java.util.List;
public interface CategoryDao {
    Category findById(Integer id);

    List<Category> findAll();

    List<Category> findEnabledCategories();

    Category findByName(String name);

    int insert(Category category);

    int delete(Integer id);

    int update(Category category);

    int updateStatus(Integer id, Integer status);

    int count();

    List<Category> findByIds(List<Integer> ids);

    List<Category> findPopularCategories(int limit);

    int countArticleByCategoryId(Integer categoryId);

}
