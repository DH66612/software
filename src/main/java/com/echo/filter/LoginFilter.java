package com.echo.filter;

import com.echo.entity.User;
import com.echo.utils.SessionUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginFilter implements Filter {

    private List<String> excludeUrls = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("LoginFilter 初始化");

        // 初始化排除路径 - 使用可变列表
        excludeUrls.add("/user/login");
        excludeUrls.add("/user/register");
        excludeUrls.add("/article/list");
        excludeUrls.add("/article/detail");
        excludeUrls.add("/login.jsp");
        excludeUrls.add("/register.html");
        excludeUrls.add("/index.jsp");
        excludeUrls.add("/css/");
        excludeUrls.add("/js/");

        System.out.println("排除路径: " + excludeUrls);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());

        System.out.println("LoginFilter 检查路径: " + path);

        // 检查是否在排除路径中
        if (isExcludeUrl(path)) {
            System.out.println("路径 " + path + " 在排除列表中，放行");
            chain.doFilter(request, response);
            return;
        }

        // 检查用户是否登录
        User currentUser = SessionUtils.getCurrentUser(httpRequest);
        if (currentUser == null) {
            System.out.println("用户未登录，跳转到登录页面");
            httpResponse.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        System.out.println("用户已登录: " + currentUser.getUsername());
        chain.doFilter(request, response);
    }

    private boolean isExcludeUrl(String path) {
        for (String excludeUrl : excludeUrls) {
            if (path.equals(excludeUrl) || path.startsWith(excludeUrl)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        System.out.println("LoginFilter 销毁");
        excludeUrls.clear();
    }
}