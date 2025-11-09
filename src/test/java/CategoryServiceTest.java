import com.echo.entity.Category;
import com.echo.service.CategoryService;
import com.echo.service.CategoryServiceImpl;

import java.util.List;

public class CategoryServiceTest {
    public static void main(String[] args) {
        try {
            CategoryService categoryService = new CategoryServiceImpl();

            System.out.println("=== 分类服务层测试 ===");

            // 测试服务层方法
            System.out.println("1. 测试 getAllCategories()...");
            List<Category> allCats = categoryService.getAllCategories();
            System.out.println("   所有分类: " + allCats.size());

            System.out.println("2. 测试 getEnabledCategories()...");
            List<Category> enabledCats = categoryService.getEnabledCategories();
            System.out.println("   启用分类: " + enabledCats.size());

            System.out.println("3. 测试 getCategoryById(1)...");
            Category cat1 = categoryService.getCategoryById(1);
            System.out.println("   分类1: " + (cat1 != null ? cat1.getName() : "null"));

            System.out.println("=== 服务层测试完成 ===");

        } catch (Exception e) {
            System.out.println("❌ 服务层测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}