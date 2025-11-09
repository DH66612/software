
import com.echo.entity.Article;
import com.echo.service.ArticleService;
import com.echo.service.ArticleServiceImpl;
import java.util.Date;

public class TestArticleInsert {
    public static void main(String[] args) {
        try {
            System.out.println("=== 开始测试文章插入 ===");

            ArticleService articleService = new ArticleServiceImpl();

            // 创建测试文章
            Article article = new Article();
            article.settitle("测试文章标题");
            article.setcontent("这是测试文章内容");
            article.setauthorid(1); // 确保这个用户ID在users表中存在
            article.setstatus(1);

            // 设置必要的时间字段
            Date now = new Date();
            article.setcreateTime(now);
            article.setupdateTime(now);
            article.setpublishTime(now);

            // 设置其他必要字段
            article.setviewCount(0);
            article.setlikeCount(0);
            article.setcommentCount(0);
            article.setSummary("测试摘要");
            article.setHtmlContent("<p>这是测试文章内容</p >");

            System.out.println("测试文章对象: " + article);

            // 测试分类ID（确保这个分类ID在categories表中存在）
            int[] categoryIds = {1};

            // 执行插入
            Article savedArticle = articleService.publishArticle(article, categoryIds);

            if (savedArticle != null && savedArticle.getid() != null) {
                System.out.println("=== 测试成功 ===");
                System.out.println("生成的文章ID: " + savedArticle.getid());
            } else {
                System.out.println("=== 测试失败 ===");
            }

        } catch (Exception e) {
            System.out.println("=== 测试异常 ===");
            System.out.println("错误信息: " + e.getMessage());
            e.printStackTrace();
        }
    }
}