import com.echo.dao.CategoryDao;
import com.echo.dao.CategoryDaoImpl;
import com.echo.service.CategoryService;
import com.echo.service.CategoryServiceImpl;
import com.echo.entity.Category;

import java.util.List;

public class CategoryFunctionTest {
    public static void main(String[] args) {
        try {
            CategoryDao categoryDao = new CategoryDaoImpl();
            CategoryService categoryService = new CategoryServiceImpl();

            System.out.println("=== 分类功能测试 ===");

            // 测试1: 检查数据库连接和表访问
            System.out.println("1. 测试数据库连接...");
            List<Category> allCategories = categoryDao.findAll();
            System.out.println("   所有分类数量: " + allCategories.size());

            // 测试2: 检查启用分类
            System.out.println("2. 测试启用分类...");
            List<com.echo.entity.Category> enabledCategories = categoryDao.findEnabledCategories();
            System.out.println("   启用分类数量: " + enabledCategories.size());

            // 测试3: 检查服务层
            System.out.println("3. 测试服务层...");
            List<com.echo.entity.Category> serviceCategories = categoryService.getEnabledCategories();
            System.out.println("   服务层返回分类数量: " + serviceCategories.size());

            // 显示分类详情
            System.out.println("4. 分类详情:");
            for (Category cat : enabledCategories) {
                System.out.println("   - ID: " + cat.getId() + ", 名称: " + cat.getName() + ", 状态: " + cat.getStatus());
            }

            System.out.println("=== 测试完成 ===");

        } catch (Exception e) {
            System.out.println("❌ 测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}