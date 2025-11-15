package com.echo.controller;

import com.echo.entity.Article;
import com.echo.entity.Comment;
import com.echo.entity.User;
import com.echo.entity.Category;
import com.echo.service.*;
import com.echo.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;


@WebServlet("/article/*")
public class ArticleController extends HttpServlet {

    private ArticleService articleService = new ArticleServiceImpl();
    private CategoryService categoryService = new CategoryServiceImpl();

    private UserServiceImpl userService = new UserServiceImpl();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        String action = (pathInfo != null) ? pathInfo.substring(1) : "";
//Get方法提交的对应的映射
        try {
            switch (action) {
                case "list":
                    showArticleList(request, response);
                    break;
                case "detail":
                    showArticleDetail(request, response);
                    break;
                case "publish":
                    showPublishPage(request, response);
                    break;
                case "edit":
                    showEditPage(request, response);
                    break;
                case "my-articles":
                    showMyArticles(request, response);
                    break;
                case "search":
                    searchArticles(request, response);
                    break;
                case "by-category":
                    showArticlesByCategory(request, response);
                    break;
                case "comment":
                    addComment(request, response);
                default:
                    // 默认显示文章列表
                    showArticleList(request, response);
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== POST请求到达ArticleController ===");
        System.out.println("请求URL: " + request.getRequestURL());
        System.out.println("请求方法: " + request.getMethod());
        System.out.println("Content-Type: " + request.getContentType());//获取请求内容类型,如JSON，HTML

        String pathInfo = request.getPathInfo();
        System.out.println("PathInfo: " + pathInfo);

        // 打印所有参数
        System.out.println("请求参数:");
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            System.out.println("  " + paramName + " = " + paramValue);
        }

        String action = (pathInfo != null) ? pathInfo.substring(1) : "";//截取路径信息

        try {
            switch (action) {
                case "publish":
                    publishArticle(request, response);
                    break;
                case "edit":
                    updateArticle(request, response);
                    break;
                case "delete":
                    deleteArticle(request, response);
                    break;
                case "like":
                    likeArticle(request, response);
                    break;
                case "unlike":
                    unlikeArticle(request, response);
                    break;
                case "comment":
                    addComment(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }

    private void addComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("=== 在ArticleController中处理评论 ===");

        String articleId = request.getParameter("articleId");
        String content = request.getParameter("content");

        System.out.println("articleId: " + articleId);
        System.out.println("content: " + content);

        try {
            if (articleId == null || content == null || content.trim().isEmpty()) {
                System.out.println("❌ 参数验证失败");
                response.sendRedirect(request.getContextPath() + "/article/detail?id=" + (articleId != null ? articleId : ""));
                return;
            }

            // 检查用户是否登录
            User currentUser = SessionUtils.getCurrentUser(request);
            if (currentUser == null) {
                System.out.println("❌ 用户未登录，无法评论");
                response.sendRedirect(request.getContextPath() + "/user/login?redirect=" +
                        java.net.URLEncoder.encode(request.getRequestURL() + "?id=" + articleId, "UTF-8"));//编码文章URL为UTF-8格式
                return;
            }

            System.out.println("当前登录用户: ID=" + currentUser.getId() + ", 用户名=" + currentUser.getUsername() + ", 昵称=" + currentUser.getNickname());

            Comment comment = new Comment();
            comment.setArticleId(Integer.parseInt(articleId));//转换文章ID为整数
            comment.setUserId(currentUser.getId()); // 使用当前登录用户的ID
            comment.setContent(content);
            comment.setStatus(1);

            System.out.println("准备发布评论，用户ID: " + currentUser.getId());

            // 发布评论
            CommentService commentService = new CommentServiceImpl();
            Comment publishedComment = commentService.publishComment(comment);
            System.out.println("✅ 评论发布成功，评论ID: " + publishedComment.getId());

            // 重定向回文章详情页
            response.sendRedirect(request.getContextPath() + "/article/detail?id=" + articleId);

        } catch (Exception e) {
            System.out.println("❌ 发生异常: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/article/detail?id=" + request.getParameter("articleId"));
        }
    }
    private void showArticleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 获取搜索关键词参数
            String keyword = request.getParameter("keyword");

            // 获取分类参数
            String categoryIdParam = request.getParameter("categoryId");
            Integer categoryId = null;
            Category currentCategory = null;

          ;


            if (categoryIdParam != null && !categoryIdParam.isEmpty()) {
                categoryId = Integer.parseInt(categoryIdParam);
                currentCategory = categoryService.getCategoryById(categoryId);
            }

            // 分页
            String pageParam = request.getParameter("page");
            int page = 1;
            int pageSize = 3;
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
            }
            // 获取文章列表和总数

            List<Article> articles;
            int totalCount;

            if (keyword != null && !keyword.trim().isEmpty()) {
                // 搜索关键词
                articles = articleService.searchArticles(keyword.trim(), page, pageSize);
                totalCount = articleService.getSearchCount(keyword.trim());//去除关键词首尾空格
                request.setAttribute("keyword", keyword.trim());

            } else if (categoryId != null) {
                // 按分类获取文章
                articles = articleService.getArticlesByCategoryid(categoryId, page, pageSize);
                totalCount = articleService.getArticleCountByCategoryId(categoryId);
            } else {
                // 获取所有文章
                articles = articleService.getPublishArticles(page, pageSize);
                totalCount = articleService.getArticleCount();
            }

            // 处理文章摘要和作者信息
            processArticleSummaries(articles);
            enrichArticleAuthorInfo(articles);
            CommentService commentService = new CommentServiceImpl();
            for (Article article : articles) {//遍历文章列表
                int commentCount = commentService.getCommentCountByArticleId(article.getid());
                article.setcommentCount(commentCount);
            }
            // 获取所有分类
            List<Category> categories = categoryService.getAllCategories();

            // 计算分页信息
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);

            // 设置请求属性
            request.setAttribute("articles", articles);
            request.setAttribute("categories", categories);
            request.setAttribute("currentCategoryId", categoryId);
            request.setAttribute("currentCategory", currentCategory);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalCount", totalCount);

            request.getRequestDispatcher("/article-list.jsp").forward(request, response);

        } catch (NumberFormatException e) {//数字格式转换异常
            request.setAttribute("error", "参数格式错误");
            request.getRequestDispatcher("/article-list.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "获取文章列表失败: " + e.getMessage());
            request.getRequestDispatcher("/article-list.jsp").forward(request, response);
        }
    }


    private void processArticleSummaries(List<Article> articles) {
        for (Article article : articles) {
            // 检查摘要是否为空
            if (article.getSummary() == null || article.getSummary().trim().isEmpty()) {
                String summary = generateSummaryFromContent(article.getcontent());
                article.setSummary(summary);
            }

            // 如果摘要为空，截取文章的前150个字作为摘要
            if (article.getSummary().length() > 150) {
                article.setSummary(article.getSummary().substring(0, 150) + "...");
            }
        }
    }





    private String generateSummaryFromContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return "这篇文章还没有摘要";
        }


        String plainText = content.replaceAll("<[^>]*>", "");


        plainText = plainText.replaceAll("\\s+", " ").trim();


        if (plainText.length() <= 100) {//截取文章摘要
            return plainText;
        } else {

            int endIndex = 100;
            for (int i = 100; i > 80; i--) {
                if (plainText.charAt(i) == '。' || plainText.charAt(i) == '！' ||//获取字符串指定位置字符
                        plainText.charAt(i) == '？' || plainText.charAt(i) == '.') {
                    endIndex = i + 1;
                    break;
                }
            }
            return plainText.substring(0, endIndex) + "...";
        }
    }

    private void enrichArticleAuthorInfo(List<Article> articles) {
        for (Article article : articles) {
            User author = userService.getUserById(article.getauthorid());
            if (author != null) {
                // 修复逻辑：如果昵称为空或空字符串，使用用户名
                if (author.getNickname() != null && !author.getNickname().trim().isEmpty()) {
                    article.setAuthorName(author.getNickname());
                } else {
                    article.setAuthorName(author.getUsername());
                }
                article.setauthorAvatar(author.getAvatar());

                // 调试信息
                System.out.println("文章ID: " + article.getid() +
                        ", 作者ID: " + article.getauthorid() +
                        ", 用户名: " + author.getUsername() +
                        ", 昵称: " + author.getNickname() +
                        ", 最终显示: " + article.getAuthorName());
            } else {
                // 如果找不到作者信息，设置默认值
                article.setAuthorName("未知作者");
                System.out.println("⚠️ 未找到文章ID " + article.getid() + " 的作者信息，作者ID: " + article.getauthorid());
            }
        }
    }





    private void showArticleDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== 开始加载文章详情 ===");

        String idStr = request.getParameter("id");
        System.out.println("文章ID参数: " + idStr);

        if (idStr == null || idStr.isEmpty()) {
            System.out.println("文章ID为空，重定向到列表");
            response.sendRedirect(request.getContextPath() + "/article/list");
            return;
        }

        try {
            Integer id = Integer.parseInt(idStr);
            System.out.println("解析后的文章ID: " + id);

            Article article = articleService.getArticleById(id);
            System.out.println("查询到的文章: " + article);

            if (article == null) {
                System.out.println("文章不存在");
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "文章不存在");
                return;
            }

            // 增加阅读量,点击一次算一次
            articleService.incrementViewCount(id);
            
            boolean hasLiked = hasLikedArticle(request, id);
            System.out.println("用户点赞状态: " + hasLiked);

            // 获取当前用户
            User currentUser = SessionUtils.getCurrentUser(request);
            System.out.println("当前用户: " + currentUser);


            CommentService commentService = new CommentServiceImpl();

            // 获取评论分页参数
            String commentPageStr = request.getParameter("commentPage");
            int commentPage = 1;
            int commentPageSize = 5;

            if (commentPageStr != null && !commentPageStr.isEmpty()) {
                try {
                    commentPage = Integer.parseInt(commentPageStr);
                    if (commentPage < 1) commentPage = 1;
                } catch (NumberFormatException e) {
                    commentPage = 1;
                }
            }

            System.out.println("评论分页参数 - 页码: " + commentPage + ", 页大小: " + commentPageSize);

            // 获取评论列表和总数
            List<Comment> comments = commentService.getCommentsByArticleId(id, commentPage, commentPageSize);
            int totalComments = commentService.getCommentCountByArticleId(id);
            int totalCommentPages = (int) Math.ceil((double) totalComments / commentPageSize);//计算评论总页数

            System.out.println("查询到的评论数量: " + comments.size());
            System.out.println("评论总数: " + totalComments);
            System.out.println("评论总页数: " + totalCommentPages);

            // 打印每条评论的详细信息
            for (Comment comment : comments) {
                System.out.println("评论ID: " + comment.getId() + ", 内容: " + comment.getContent() + ", 作者: " + comment.getAuthorName());
            }

            // 设置评论相关属性
            request.setAttribute("comments", comments);
            request.setAttribute("currentCommentPage", commentPage);
            request.setAttribute("totalCommentPages", totalCommentPages);
            request.setAttribute("totalComments", totalComments);

            // 设置文章和用户属性
            request.setAttribute("article", article);
            request.setAttribute("currentUser", currentUser);

            System.out.println("=== 文章详情加载完成，准备转发 ===");

            // 转发到详情页面
            request.getRequestDispatcher("/article_detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            System.out.println("文章ID格式错误: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "文章ID格式错误");//返回400错误
        } catch (Exception e) {
            System.out.println("加载文章详情异常: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "获取文章详情失败: " + e.getMessage());
            request.getRequestDispatcher("/article_detail.jsp").forward(request, response);
        }
    }
   //我的文章
    private void showMyArticles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== 开始加载我的文章 ===");

        // 检查用户是否登录
        User currentUser = SessionUtils.getCurrentUser(request);
        if (currentUser == null) {
            System.out.println("用户未登录，重定向到登录页面");
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }

        System.out.println("当前用户ID: " + currentUser.getId() + ", 用户名: " + currentUser.getUsername());

        String pageStr = request.getParameter("page");
        int page = 1;
        int pageSize = 10;

        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        try {
            // 获取用户的文章
            List<Article> articles = articleService.getArticlesByUserid(currentUser.getId(), page, pageSize);
            System.out.println("获取到的文章数量: " + articles.size());

            // 打印每篇文章的详细信息
            for (Article article : articles) {
                System.out.println("文章ID: " + article.getid() + ", 标题: " + article.gettitle() + ", 作者ID: " + article.getauthorid());
            }

            int totalCount = articleService.getArticleCountByUserId(currentUser.getId());
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);

            System.out.println("文章总数: " + totalCount + ", 总页数: " + totalPages);

            // 处理文章摘要和作者信息
            processArticleSummaries(articles);
            enrichArticleAuthorInfo(articles);

            // 获取分类列表
            List<Category> categories = categoryService.getEnabledCategories();

            // 设置请求属性
            request.setAttribute("articles", articles);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("categories", categories);

            System.out.println("=== 我的文章加载完成，准备转发 ===");

            request.getRequestDispatcher("/my-article.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println("加载我的文章异常: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "获取文章列表失败: " + e.getMessage());
            request.getRequestDispatcher("/my-article.jsp").forward(request, response);
        }
    }

    private void searchArticles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        String pageStr = request.getParameter("page");

        int page = 1;
        int pageSize = 10;

        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        List<Article> articles;
        int totalCount;

        if (keyword != null && !keyword.trim().isEmpty()) {
            articles = articleService.searchArticles(keyword.trim(), page, pageSize);
            totalCount = articleService.getSearchCount(keyword.trim());
        } else {
            articles = articleService.getPublishArticles(page, pageSize);
            totalCount = articleService.getArticleCount();
        }

        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        // 获取分类列表
        List<Category> categories = categoryService.getEnabledCategories();

        request.setAttribute("articles", articles);//设置请求属性
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("keyword", keyword);
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("/WEB-INF/views/article/search.jsp").forward(request, response);
    }


    private void showArticlesByCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryIdStr = request.getParameter("id");
        String pageStr = request.getParameter("page");

        if (categoryIdStr == null || categoryIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/article/list");
            return;
        }

        int page = 1;
        int pageSize = 10;

        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        try {
            Integer categoryId = Integer.parseInt(categoryIdStr);

            // 获取分类信息
            Category category = categoryService.getCategoryById(categoryId);
            if (category == null || !category.isEnabled()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "分类不存在");
                return;
            }

            // 获取分类下的文章
            List<Article> articles = articleService.getArticlesByCategoryid(categoryId, page, pageSize);
            int totalCount = articleService.getArticleCountByCategoryId(categoryId);
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);

            // 获取分类列表
            List<Category> categories = categoryService.getEnabledCategories();

            request.setAttribute("articles", articles);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("currentCategory", category);
            request.setAttribute("categories", categories);

            request.getRequestDispatcher("/WEB-INF/views/article/category.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "分类ID格式错误");
        }
    }


    private void publishArticle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 检查用户是否登录
        User currentUser = SessionUtils.getCurrentUser(request);
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String[] categoryIds = request.getParameterValues("categoryIds");
        String allowComment = request.getParameter("allowComment");
        String summary = request.getParameter("summary"); // 新增摘要参数
        try {
            // 数据验证
            if (title == null || title.trim().isEmpty()) {
                throw new RuntimeException("文章标题不能为空");
            }

            if (content == null || content.trim().isEmpty()) {
                throw new RuntimeException("文章内容不能为空");
            }

            Article article = new Article();
            article.settitle(title.trim());
            article.setcontent(content.trim());
            article.setSummary(summary); // 设置摘要字段
            article.setauthorid(currentUser.getId());// 转换分类ID
            int[] categoryIdArray = null;
            if (categoryIds != null && categoryIds.length > 0) {
                categoryIdArray = new int[categoryIds.length];
                for (int i = 0; i < categoryIds.length; i++) {
                    categoryIdArray[i] = Integer.parseInt(categoryIds[i]);
                }
            }

            // 发布文章
            Article publishedArticle = articleService.publishArticle(article, categoryIdArray);

            // 发布成功，跳转到文章详情页
            request.getSession().setAttribute("successMessage", "文章发布成功！");
            response.sendRedirect(request.getContextPath() + "/article/detail?id=" + publishedArticle.getid());
            return;
        } catch (NumberFormatException e) {
            request.setAttribute("error", "分类ID格式错误");
            showPublishPage(request, response);
        } catch (RuntimeException e) {
            // 发布失败，返回发布页面并显示错误信息
            request.setAttribute("error", e.getMessage());
            request.setAttribute("title", title);
            request.setAttribute("content", content);
            request.setAttribute("categoryIds", categoryIds);
            request.setAttribute("summary", summary); // 保留摘要内容
            // 重新加载分类列表
            List<Category> categories = categoryService.getEnabledCategories();
            request.setAttribute("categories", categories);
            request.setAttribute("action", "publish");

            request.getRequestDispatcher("/publish-article.jsp").forward(request, response);//跳转到文章发布页面
        }//找到名为 publish-article.jsp 的页面,将当前的请求和响应对象原封不动传递给目标页面
    }


    private void updateArticle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 检查用户是否登录
        User currentUser = SessionUtils.getCurrentUser(request);
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }String idStr = request.getParameter("id");//获取编辑的次数
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String[] categoryIds = request.getParameterValues("categoryIds");
        String allowComment = request.getParameter("allowComment");
        String status = request.getParameter("status");
        String summary = request.getParameter("summary");
        try {
            if (idStr == null || idStr.isEmpty()) {
                throw new RuntimeException("文章ID不能为空");
            }

            Integer id = Integer.parseInt(idStr);

            // 检查文章是否存在和权限
            Article existingArticle = articleService.getArticleById(id);
            if (existingArticle == null) {
                throw new RuntimeException("文章不存在");
            }

            if (!existingArticle.isOwnedBy(currentUser) && !currentUser.isAdmin()) {
                throw new RuntimeException("没有权限编辑此文章");
            }

            // 数据验证
            if (title == null || title.trim().isEmpty()) {
                throw new RuntimeException("文章标题不能为空");
            }

            if (content == null || content.trim().isEmpty()) {
                throw new RuntimeException("文章内容不能为空");
            }

            Article article = new Article();
            article.setid(id);
            article.setSummary(summary); // 设置摘要字段
            article.settitle(title.trim());
            article.setcontent(content.trim());// 设置状态（只有管理员可以修改状态）
            if (currentUser.isAdmin() && status != null) {
                try {
                    article.setstatus(Integer.parseInt(status));//设置文章状态值
                } catch (NumberFormatException e) {//捕获数字格式异常
                    // 忽略状态解析错误
                }
            }

            // 转换分类ID
            int[] categoryIdArray = null;
            if (categoryIds != null && categoryIds.length > 0) {
                categoryIdArray = new int[categoryIds.length];
                for (int i = 0; i < categoryIds.length; i++) {
                    categoryIdArray[i] = Integer.parseInt(categoryIds[i]);
                }
            }

            // 更新文章
            Article updatedArticle = articleService.updateArticle(article, categoryIdArray);

            // 更新成功，跳转到文章详情页
            request.getSession().setAttribute("successMessage", "文章更新成功！");
            response.sendRedirect(request.getContextPath() + "/article/detail?id=" + updatedArticle.getid());
            return;
        } catch (NumberFormatException e) {
            request.setAttribute("error", "文章ID或分类ID格式错误");
            showEditPage(request, response);
        } catch (RuntimeException e) {
            // 更新失败，返回编辑页面并显示错误信息
            request.setAttribute("error", e.getMessage());
            request.setAttribute("title", title);
            request.setAttribute("content", content);
            request.setAttribute("categoryIds", categoryIds);

            // 重新加载分类列表
            List<Category> categories = categoryService.getEnabledCategories();
            request.setAttribute("categories", categories);
            request.setAttribute("action", "edit");

            request.getRequestDispatcher("/publish-article.jsp").forward(request, response);
        }
    }


    private void showEditPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("===  开始加载编辑页面 ===");

        // 检查用户是否登录
        User currentUser = SessionUtils.getCurrentUser(request);
        if (currentUser == null) {
            System.out.println(" 用户未登录，重定向到登录页面");
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }

        String idStr = request.getParameter("id");
        System.out.println("编辑文章ID参数: " + idStr);

        if (idStr == null || idStr.isEmpty()) {
            System.out.println("❌ 文章ID为空，重定向到我的文章页面");
            response.sendRedirect(request.getContextPath() + "/article/my-articles");
            return;
        }

        try {
            Integer id = Integer.parseInt(idStr);
            System.out.println("解析后的文章ID: " + id);

            Article article = articleService.getArticleById(id);
            System.out.println("查询到的文章: " + article);

            if (article == null) {
                System.out.println("❌ 文章不存在");
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "文章不存在");//返回404错误，文章不存在
                return;
            }

            System.out.println("🔍 权限检查 - 文章作者ID: " + article.getauthorid() + ", 当前用户ID: " + currentUser.getId());
            System.out.println("🔍 用户角色 - 是否管理员: " + currentUser.isAdmin());

            // 权限检查：只有作者或管理员可以编辑
            if (!article.isOwnedBy(currentUser) && !currentUser.isAdmin()) {
                System.out.println("❌ 没有权限编辑此文章");
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "没有权限编辑此文章");//返回403错误，无编辑权限
                return;
            }

            // 获取分类列表
            List<Category> categories = categoryService.getEnabledCategories();

            request.setAttribute("article", article);
            request.setAttribute("categories", categories);
            request.setAttribute("action", "edit");

            System.out.println("✅ 编辑页面加载完成，准备转发");

            request.getRequestDispatcher("/publish-article.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            System.out.println("❌ 文章ID格式错误: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "文章ID格式错误");//返回400错误，ID格式错误
        } catch (Exception e) {
            System.out.println("❌ 加载编辑页面异常: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "加载编辑页面失败");//返回500错误，加载失败
        }
    }


    private void deleteArticle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== 🗑️ 开始删除文章 ===");

        // 检查用户是否登录
        User currentUser = SessionUtils.getCurrentUser(request);
        if (currentUser == null) {
            System.out.println("❌ 用户未登录");
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }

        String idStr = request.getParameter("id");
        System.out.println("删除文章ID参数: " + idStr);

        try {
            if (idStr == null || idStr.isEmpty()) {
                throw new RuntimeException("文章ID不能为空");
            }

            Integer id = Integer.parseInt(idStr);
            System.out.println("解析后的文章ID: " + id);

            // 检查文章是否存在
            Article article = articleService.getArticleById(id);
            if (article == null) {
                System.out.println("❌ 文章不存在");
                throw new RuntimeException("文章不存在");
            }

            System.out.println("🔍 权限检查 - 文章作者ID: " + article.getauthorid() + ", 当前用户ID: " + currentUser.getId());
            System.out.println("🔍 用户角色 - 是否管理员: " + currentUser.isAdmin());

            // 检查权限
            if (!article.isOwnedBy(currentUser) && !currentUser.isAdmin()) {
                System.out.println("❌ 没有权限删除此文章");
                throw new RuntimeException("没有权限删除此文章");
            }

            // 删除文章
            System.out.println("开始删除文章...");
            boolean success = articleService.deleteArticle(id);

            if (success) {
                System.out.println("✅ 文章删除成功");
                request.getSession().setAttribute("message", "文章删除成功！");
            } else {
                System.out.println("❌ 文章删除失败");
                throw new RuntimeException("文章删除失败");
            }

            // 根据用户权限跳转到不同的页面
            if (currentUser.isAdmin()) {
                System.out.println("跳转到文章列表页面");
                response.sendRedirect(request.getContextPath() + "/article/list");
            } else {
                System.out.println("跳转到我的文章页面");
                response.sendRedirect(request.getContextPath() + "/article/my-articles");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ 文章ID格式错误: " + e.getMessage());
            request.getSession().setAttribute("error", "文章ID格式错误");
            response.sendRedirect(request.getContextPath() + "/article/my-articles");
        } catch (RuntimeException e) {
            System.out.println("❌ 删除文章异常: " + e.getMessage());
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/article/my-articles");
        }
    }

    private void likeArticle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");

        try {
            if (idStr == null || idStr.isEmpty()) {
                throw new RuntimeException("文章ID不能为空");
            }

            Integer id = Integer.parseInt(idStr);

            // 检查是否已经点赞（Cookie方案）
            if (hasLikedArticle(request, id)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"您已经点赞过此文章\"}");
                return;
            }

            // 执行点赞
            boolean success = articleService.likeArticle(id);

            if (success) {
                // 设置点赞Cookie
                setLikedCookie(response, id);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"success\": true}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"点赞失败\"}");
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    // 保留原有的取消点赞方法
    private void unlikeArticle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 检查用户是否登录
        User currentUser = SessionUtils.getCurrentUser(request);
        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "请先登录");
            return;
        }

        String idStr = request.getParameter("id");

        try {
            if (idStr == null || idStr.isEmpty()) {
                throw new RuntimeException("文章ID不能为空");
            }

            Integer id = Integer.parseInt(idStr);
            boolean success = articleService.unlikeArticle(id);

            if (success) {
                // 移除点赞Cookie
                removeLikedCookie(response, id);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"success\": true}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"取消点赞失败\"}");
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    // 辅助方法：检查是否已经点赞
    private boolean hasLikedArticle(HttpServletRequest request, Integer articleId) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return false;
        }

        String likeCookieName = "article_liked_" + articleId;
        for (Cookie cookie : cookies) {
            if (likeCookieName.equals(cookie.getName())) {
                return "true".equals(cookie.getValue());
            }
        }
        return false;
    }

    // 辅助方法：设置点赞Cookie
    private void setLikedCookie(HttpServletResponse response, Integer articleId) {
        Cookie likeCookie = new Cookie("article_liked_" + articleId, "true");
        likeCookie.setMaxAge(30 * 24 * 60 * 60); // 30天有效期
        likeCookie.setPath("/");
        likeCookie.setHttpOnly(true);
        response.addCookie(likeCookie);
    }

    // 辅助方法：移除点赞Cookie
    private void removeLikedCookie(HttpServletResponse response, Integer articleId) {
        Cookie likeCookie = new Cookie("article_liked_" + articleId, "");
        likeCookie.setMaxAge(0); // 立即过期
        likeCookie.setPath("/");
        response.addCookie(likeCookie);
    }



    private void showPublishPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 检查用户是否登录
        User currentUser = SessionUtils.getCurrentUser(request);
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }

        // 获取分类列表
        List<Category> categories = categoryService.getEnabledCategories();

        request.setAttribute("categories", categories);
        request.setAttribute("action", "publish");

        request.getRequestDispatcher("/publish-article.jsp").forward(request, response);
    }


    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws ServletException, IOException {

        e.printStackTrace();
        if (response.isCommitted()) {
            // 响应已提交，无法转发，记录日志并尝试发送错误信息到客户端（如果可能）
            System.err.println("响应已提交，无法转发错误页面: " + e.getMessage());
            // 可以尝试使用 writer 写入错误信息
            try {
                response.getWriter().write("服务器内部错误: " + e.getMessage());//输出服务器内部错误信息
            } catch (IOException ex) {
                // 忽略
            }
            return;
        }
        request.setAttribute("error", "服务器内部错误: " + e.getMessage());//存储服务器错误信息
        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }


}