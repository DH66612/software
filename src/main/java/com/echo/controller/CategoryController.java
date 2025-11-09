//一直改不动，干脆放弃了，后面再来补吧
package com.echo.controller;

import com.echo.entity.Category;
import com.echo.service.CategoryService;
import com.echo.service.CategoryServiceImpl;
import com.echo.utils.jsonUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/category/*")
public class CategoryController extends HttpServlet {

    private CategoryService categoryService;

    @Override
    public void init() throws ServletException {
        this.categoryService = new CategoryServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        String action = (pathInfo != null) ? pathInfo.substring(1) : "";

        switch (action) {
            case "list":
                getCategoryList(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        String action = (pathInfo != null) ? pathInfo.substring(1) : "";

        switch (action) {
            case "add":
                addCategory(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    private void getCategoryList(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            List<Category> categories = categoryService.getAllCategories();

            // 设置响应类型为JSON
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(jsonUtils.toJson(categories));

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"获取分类列表失败\"}");
        }
    }


    private void addCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            String name = request.getParameter("name");
            String description = request.getParameter("description");

            // 参数验证
            if (name == null || name.trim().isEmpty()) {
                sendJsonResponse(response, false, "分类名称不能为空");
                return;
            }

            // 检查分类是否已存在
            Category existingCategory = categoryService.getCategoryByName(name.trim());
            if (existingCategory != null) {
                sendJsonResponse(response, false, "分类名称已存在");
                return;
            }

            // 创建分类
            Category category = new Category();
            category.setName(name.trim());
            category.setDescription(description != null ? description.trim() : null);

            // 保存分类
            boolean success = categoryService.saveCategory(category);

            if (success) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "分类添加成功");
                result.put("category", category);

                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(jsonUtils.toJson(result));
            } else {
                sendJsonResponse(response, false, "添加分类失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(response, false, "系统错误: " + e.getMessage());
        }
    }

    /**
     * 发送JSON响应
     */
    private void sendJsonResponse(HttpServletResponse response, boolean success, String message)
            throws IOException {

        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", message);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonUtils.toJson(result));
    }
}