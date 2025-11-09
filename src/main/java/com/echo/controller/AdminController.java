//没用上....
package com.echo.controller;
import com.echo.entity.Article;
import com.echo.entity.User;
import com.echo.service.ArticleService;
import com.echo.service.ArticleServiceImpl;
import com.echo.service.UserService;
import com.echo.service.UserServiceImpl;
import com.echo.utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import java.util.List;
@WebServlet("/admin/")
public class AdminController {
    private  ArticleService articleService=new ArticleServiceImpl();
    private UserService userService=new UserServiceImpl();
    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        String pathInfo=request.getPathInfo();
        String action=(pathInfo==null)?"":pathInfo.substring(1);
        if(!checkAdminPermission(request,response)){
            return;}

        try {
            switch (action) {
                case "dashboard":
                    showDashboard(request, response);
                    break;
                case "users":
                    showUserList(request, response);
                    break;
                case "articles":
                    showArticleList(request, response);
                    break;
                case "categories":
                    showCategoryList(request, response);
                    break;
                case "comments":
                    showCommentList(request, response);
                    break;
                case "":
                case "index":
                    showDashboard(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        String action = (pathInfo != null) ? pathInfo.substring(1) : "";

        // 检查管理员权限
        if (!checkAdminPermission(request, response)) {
            return;
        }   try {
            switch (action) {
                case "users/disable":
                    disableUser(request, response);
                    break;
                case "users/enable":
                    enableUser(request, response);
                    break;
                case "articles/delete":
                    deleteArticle(request, response);
                    break;
                case "articles/update-status":
                    updateArticleStatus(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }


    private boolean checkAdminPermission(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        User currentUser = SessionUtils.getCurrentUser(request);
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/user/login");
            return false;
        }

        if (!currentUser.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "需要管理员权限");
            return false;
        }

        return true;
    }
    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 获取统计数据
        int userCount = userService.getAllUserCount();
        int articleCount = articleService.getArticleCount();
        int todayArticles = getTodayArticlesCount(); // 需要实现这个方法

        request.setAttribute("userCount", userCount);
        request.setAttribute("articleCount", articleCount);
        request.setAttribute("todayArticles", todayArticles);

        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }


    private void showUserList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pageStr = request.getParameter("page");
        String keyword = request.getParameter("keyword");

        int page = 1;
        int pageSize = 10;
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        List<User> users;
        int totalCount;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // 搜索用户
            users = userService.searchUser(keyword.trim(), page, pageSize);
            totalCount = userService.getAllUserCount(); // 需要实现搜索计数方法
        } else {
            // 获取所有用户
            users = userService.getAllUser(page, pageSize);
            totalCount = userService.getAllUserCount();
        }

        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        request.setAttribute("users", users);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("keyword", keyword);
        request.setAttribute("totalCount", totalCount);

        request.getRequestDispatcher("/WEB-INF/views/admin/user-list.jsp").forward(request, response);
    }


    private void showArticleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pageStr = request.getParameter("page");
        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");

        int page = 1;
        int pageSize = 10;

        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        List<Article> articles;
        int totalCount;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // 搜索文章
            articles = articleService.searchArticles(keyword.trim(), page, pageSize);
            totalCount = articleService.getSearchCount(keyword.trim());
        } else {
            // 获取所有文章
            articles = articleService.getPublishArticles(page, pageSize); // 需要实现获取所有文章的方法
            totalCount = articleService.getArticleCount();
        }int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        request.setAttribute("articles", articles);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("keyword", keyword);
        request.setAttribute("status", status);
        request.setAttribute("totalCount", totalCount);

        request.getRequestDispatcher("/article/list").forward(request, response);
    }


    private void showCategoryList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("message", "分类管理功能开发中...");
        request.getRequestDispatcher("/WEB-INF/views/admin/category-list.jsp").forward(request, response);
    }


    private void showCommentList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("message", "评论管理功能开发中...");
        request.getRequestDispatcher("/WEB-INF/views/admin/comment-list.jsp").forward(request, response);
    }


    private void disableUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userIdStr = request.getParameter("userId");

        try {
            Integer userId = Integer.parseInt(userIdStr);
            boolean success = userService.disableUser(userId);

            if (success) {
                request.getSession().setAttribute("successMessage", "用户禁用成功");
            } else {
                request.getSession().setAttribute("errorMessage", "用户禁用失败");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "用户ID格式错误");
        } catch (RuntimeException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }


    private void enableUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userIdStr = request.getParameter("userId");

        try {
            Integer userId = Integer.parseInt(userIdStr);
            boolean success = userService.enableUser(userId);

            if (success) {
                request.getSession().setAttribute("successMessage", "用户启用成功");
            } else {
                request.getSession().setAttribute("errorMessage", "用户启用失败");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "用户ID格式错误");
        } catch (RuntimeException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
    private void deleteArticle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String articleIdStr = request.getParameter("articleId");

        try {
            Integer articleId = Integer.parseInt(articleIdStr);
            boolean success = articleService.deleteArticle(articleId);

            if (success) {
                request.getSession().setAttribute("successMessage", "文章删除成功");
            } else {
                request.getSession().setAttribute("errorMessage", "文章删除失败");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "文章ID格式错误");
        } catch (RuntimeException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/articles");
    }


    private void updateArticleStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String articleIdStr = request.getParameter("articleId");
        String statusStr = request.getParameter("status");

        try {
            Integer articleId = Integer.parseInt(articleIdStr);
            Integer status = Integer.parseInt(statusStr);

            // 需要实现更新文章状态的方法
            // boolean success = articleService.updateArticleStatus(articleId, status);
            boolean success = true; // 临时占位

            if (success) {
                request.getSession().setAttribute("successMessage", "文章状态更新成功");
            } else {
                request.getSession().setAttribute("errorMessage", "文章状态更新失败");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "参数格式错误");
        } catch (RuntimeException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/articles");
    }


    private int getTodayArticlesCount() {

        return 0;
    }


    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws ServletException, IOException {

        e.printStackTrace();
        request.setAttribute("error", "系统错误: " + e.getMessage());
        request.getRequestDispatcher("/WEB-INF/views/admin/error.jsp").forward(request, response);
    }
}


