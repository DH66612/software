package com.echo.filter;
import com.echo.entity.User;
import com.echo.utils.SessionUtils;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebFilter;
@WebFilter(
        filterName = "AdminFilter",
        urlPatterns = "/admin/*"//拦截所有后台管理路径
)
public class AdminFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
       System.out.println("管理员权限初始化中");
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String relativePath = uri.substring(contextPath.length());
        System.out.println("管理员检查路径:"+relativePath);
        User currentUser=SessionUtils.getCurrentUser(req);
        if(currentUser==null){
            System.out.println("用户未登录");
            resp.sendRedirect(req.getContextPath()+"/user/login");
            return;
        }
        if(currentUser.getRole()!=1) {
            System.out.println("您不是管理员," + currentUser.getUsername());
            resp.sendRedirect(req.getContextPath()+"/user/login");
            return;
        }
        System.out.println("管理员身份："+currentUser.getUsername());
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
        System.out.println("管理过滤器销毁中");

    }

}
