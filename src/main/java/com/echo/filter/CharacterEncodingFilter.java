package com.echo.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

@WebFilter(
        filterName = "CharacterEncodingFilter",//过滤器名称
        urlPatterns = "/*",  //URL匹配模式
        initParams = {
                @WebInitParam(name = "encoding", value = "UTF-8"),//初始化参数，这里的作用是告诉servlet如何配置，下面的初始化是在类里执行
                @WebInitParam(name = "forceEncoding", value = "true")
        }
)//可以避免在web.xml写很多配置
public class CharacterEncodingFilter implements Filter {

    private String encoding = "UTF-8";//将编码统一为UTF-8
    private boolean forceEncoding = true;//强制所有编码为默认设置
//读取配置初始化参数

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("字符编码过滤器初始化...");

        // 读取web.xml中配置的编码参数
        String encodingParam = filterConfig.getInitParameter("encoding");
        if (encodingParam != null && !encodingParam.trim().isEmpty()) {
            this.encoding = encodingParam;
        }

        // 读取是否强制设置编码的参数
        String forceEncodingParam = filterConfig.getInitParameter("forceEncoding");
        if (forceEncodingParam != null) {
            this.forceEncoding = Boolean.parseBoolean(forceEncodingParam);
        }

        System.out.println("字符编码过滤器配置: encoding=" + encoding + ", forceEncoding=" + forceEncoding);
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        System.out.println("字符编码过滤器处理请求: " + encoding);

        // 1. 设置请求编码
        if (forceEncoding || request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(encoding);
        }

        // 2. 设置响应编码
        if (forceEncoding || response.getCharacterEncoding() == null) {
            response.setCharacterEncoding(encoding);
        }

        // 3. 继续执行过滤器链（传递给下一个Filter或最终的Servlet）
        chain.doFilter(request, response);//调用下一个过滤器：登录过滤器

        System.out.println("字符编码过滤器处理完成");
    }

    /**
     * 销毁方法 - 清理资源
     */
    @Override
    public void destroy() {
        System.out.println("字符编码过滤器销毁...");
        // 没有需要清理的资源
    }
}