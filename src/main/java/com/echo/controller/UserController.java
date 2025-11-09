package com.echo.controller;

import com.echo.entity.User;
import com.echo.service.UserService;
import com.echo.service.UserServiceImpl;
import com.echo.utils.SessionUtils;
import com.echo.utils.jsonUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/user/*")
@MultipartConfig(
        maxFileSize = 2 * 1024 * 1024,      // 2MB
        maxRequestSize = 5 * 1024 * 1024,   // 5MB
        fileSizeThreshold = 1024 * 1024     // 1MB
)
public class UserController extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        // 初始化UserService
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        String action = (pathInfo != null) ? pathInfo.substring(1) : "";

        System.out.println("UserController GET 请求: " + action);

        switch (action) {
            case "login":
                showLoginPage(request, response);
                break;
            case "register":
                showRegisterPage(request, response);
                break;
            case "profile":
                showProfilePage(request, response);
                break;
            case "logout":
                logout(request, response);
                break;
            case "password":
                showChangePasswordPage(request, response);
                break;
            default:
                // 默认跳转到登录页面
                response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }
    //更新用户头像
    private void uploadAvatar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 检查用户是否登录
        User currentUser = SessionUtils.getCurrentUser(request);
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 检查是否为multipart请求
        if (!isMultipartRequest(request)) {
            request.setAttribute("error", "请选择有效的图片文件");
            showProfilePage(request, response);
            return;
        }

        try {
            // 使用Servlet 3.0的文件上传API
            Part filePart = request.getPart("avatar");

            // 验证文件
            if (filePart == null || filePart.getSize() == 0) {
                request.setAttribute("error", "请选择头像文件");
                showProfilePage(request, response);
                return;
            }

            // 验证文件类型
            String contentType = filePart.getContentType();
            if (!contentType.startsWith("image/")) {
                request.setAttribute("error", "只能上传图片文件");
                showProfilePage(request, response);
                return;
            }

            // 验证文件大小（限制为2MB）
            if (filePart.getSize() > 2 * 1024 * 1024) {
                request.setAttribute("error", "头像文件大小不能超过2MB");
                showProfilePage(request, response);
                return;
            }

            // 生成唯一文件名
            String fileName = generateAvatarFileName(currentUser.getId(),
                    getFileExtension(filePart));

            // 保存路径，在我的webapp的upload目录下
            String uploadPath = getServletContext().getRealPath("/uploads/avatars");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String filePath = uploadPath + File.separator + fileName;
            filePart.write(filePath);

            // 生成访问URL（相对路径）
            String avatarUrl = request.getContextPath() + "/uploads/avatars/" + fileName;

            // 更新数据库中的头像URL
            boolean success = userService.updateAvatar(currentUser.getId(), avatarUrl);

            if (success) {
                // 更新session中的用户信息
                User updatedUser = userService.getUserById(currentUser.getId());
                SessionUtils.setCurrentUser(request, updatedUser);

                // 弹出成功消息
                HttpSession session = request.getSession();
                session.setAttribute("message", "头像更新成功");
            } else {
                request.setAttribute("error", "头像更新失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "头像上传失败: " + e.getMessage());
        }

        // 设置头像后，重定向到个人资料页面
        response.sendRedirect(request.getContextPath() + "/user/profile");
    }

    private boolean isMultipartRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.startsWith("multipart/form-data");
    }


    private String generateAvatarFileName(Integer userId, String extension) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return "avatar_" + userId + "_" + timestamp + extension;
    }
//获得头像扩展名
    private String getFileExtension(Part filePart) {
        String fileName = getFileName(filePart);
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return ".jpg"; // 默认扩展名
    }

  //获得头像文件名
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] elements = contentDisposition.split(";");
        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                return element.substring(element.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
    //处理Post方式提交的前端信息
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        String action = (pathInfo != null) ? pathInfo.substring(1) : "";

        System.out.println("UserController POST 请求: " + action);

        switch (action) {
            case "login":
                login(request, response);
                break;
            case "register":
                register(request, response);
                break;
            case "update":
                updateProfile(request, response);
                break;
            case "password":
                changePassword(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

   //处理登录页面
    private void showLoginPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 判断用户的登录情况
        User currentUser = SessionUtils.getCurrentUser(request);
        if (currentUser != null) {
            response.sendRedirect(request.getContextPath() + "/article-list.jsp");
            return;
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);//转发请求到登录页面
    }


    private void showRegisterPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 判断用户的登录情况
        User currentUser = SessionUtils.getCurrentUser(request);
        if (currentUser != null) {
            response.sendRedirect(request.getContextPath() + "/article-list.jsp");
            return;
        }

        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }


    private void showProfilePage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 检查用户是否登录，如果没登录，重定向到登录页面
        User currentUser = SessionUtils.getCurrentUser(request);
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 处理会话中的消息
        HttpSession session = request.getSession();
        String message = (String) session.getAttribute("message");
        if (message != null) {
            request.setAttribute("message", message);
            session.removeAttribute("message"); //撤销信息
        }

        // 重新从数据库获取最新用户信息
        User user = userService.getUserById(currentUser.getId());
        if (user != null) {
            request.setAttribute("user", user);
            // 更新会话中的用户信息
            SessionUtils.setCurrentUser(request, user);
        } else {
            request.setAttribute("user", currentUser);
        }

        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }
   //修改密码页面
    private void showChangePasswordPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 检查用户是否登录
        User currentUser = SessionUtils.getCurrentUser(request);
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        request.getRequestDispatcher("/change-password.jsp").forward(request, response);
    }


    private void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember"); // 记住我功能

        System.out.println("用户登录尝试: " + username);

        try {

            if (username == null || username.trim().isEmpty()) {
                request.setAttribute("error", "用户名不能为空");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            if (password == null || password.isEmpty()) {
                request.setAttribute("error", "密码不能为空");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            // 2. 调用Service进行登录验证
            User user = userService.login(username, password);

            // 3. 登录成功，设置Session
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);//将用户对象·存入·会话

            // 4. 设置会话超时时间（如果选择了记住我）
            if ("on".equals(remember)) {
                session.setMaxInactiveInterval(7 * 24 * 60 * 60); // 7天
            } else {
                session.setMaxInactiveInterval(30 * 60); // 30分钟
            }

            System.out.println("用户登录成功: " + username);

            // 5. 重定向到首页或之前访问的页面
            String redirectUrl = request.getParameter("redirect");
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                response.sendRedirect(redirectUrl);
            } else {
                response.sendRedirect(request.getContextPath() + "/article-list.jsp");
            }

        } catch (RuntimeException e) {
            // 登录失败
            System.out.println("用户登录失败: " + e.getMessage());
            request.setAttribute("error", e.getMessage());//存储错误信息到请求
            request.setAttribute("username", username); // 显示用户信息到请求
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }/**
     * 处理用户注册
     */
    private void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");//获取用户名参数
        String password = request.getParameter("password");//获取密码参数
        String confirmPassword = request.getParameter("confirmPassword");//获取确认密码参数
        String email = request.getParameter("email");//获取邮箱参数

        System.out.println("用户注册尝试: " + username);

        try {
            // 1. 参数验证
            if (username == null || username.trim().isEmpty()) {
                request.setAttribute("error", "用户名不能为空");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            if (password == null || password.isEmpty()) {
                request.setAttribute("error", "密码不能为空");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            if (!password.equals(confirmPassword)) {
                request.setAttribute("error", "两次输入的密码不一致");
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }if (email == null || email.trim().isEmpty() || !email.contains("@")) {
                request.setAttribute("error", "邮箱格式不正确");
                request.setAttribute("username", username);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            // 2. 创建用户对象
            User user = new User();
            user.setUsername(username.trim());
            user.setPassword(password);
            user.setEmail(email.trim());

            // 3. 调用Service进行注册
            User registeredUser = userService.register(user);

            System.out.println("用户注册成功: " + username);

            // 4. 注册成功后自动登录
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", registeredUser);

            // 5. 设置成功消息并跳转到首页
            request.setAttribute("message", "注册成功！欢迎加入回声网络");
            response.sendRedirect(request.getContextPath() + "/article-list.jsp");

        } catch (RuntimeException e) {
            // 注册失败
            System.out.println("用户注册失败: " + e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }


    private void updateProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 检查用户是否登录
        User currentUser = SessionUtils.getCurrentUser(request);
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String nickname = request.getParameter("nickname");
        String email = request.getParameter("email");

        try {
            // 1. 参数验证
            if (email == null || email.trim().isEmpty() || !email.contains("@")) {
                request.setAttribute("error", "邮箱格式不正确");
                request.getRequestDispatcher("/profile.jsp").forward(request, response);
                return;
            }

            // 2. 创建更新后的用户对象
            User updatedUser = new User();
            updatedUser.setId(currentUser.getId());
            updatedUser.setNickname(nickname);
            updatedUser.setEmail(email.trim());
            // 注意：头像通过单独接口更新，这里不处理头像

            // 3. 调用Service更新资料
            User resultUser = userService.updateProfile(updatedUser);

            // 4. 更新Session中的用户信息
            SessionUtils.setCurrentUser(request, resultUser);

            System.out.println("用户资料更新成功: " + currentUser.getUsername());

            // 5. 使用重定向避免重复提交
            HttpSession session = request.getSession();
            session.setAttribute("message", "资料更新成功");
            response.sendRedirect(request.getContextPath() + "/user/profile");//重定向

        } catch (RuntimeException e) {
            // 更新失败
            System.out.println("用户资料更新失败: " + e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        }
    }

    /**
     * 修改密码
     */

    /**
     * 修改密码 - 支持AJAX和传统表单两种方式
     */
    private void changePassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 检查用户是否登录
        User currentUser = SessionUtils.getCurrentUser(request);
        if (currentUser == null) {
            // 如果是AJAX请求，返回JSON响应
            if (isAjaxRequest(request)) {
                sendJsonResponse(response, false, "用户未登录");//检查AJAX请求，未登录则返回提示
                return;
            } else {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }
        }

        String oldPassword = request.getParameter("oldPassword");//获取旧密码参数
        String newPassword = request.getParameter("newPassword");//获取新密码参数
        String confirmPassword = request.getParameter("confirmPassword");

        try {
            // 1. 参数验证
            if (oldPassword == null || oldPassword.isEmpty()) {
                handleError(request, response, "旧密码不能为空", false);
                return;
            }

            if (newPassword == null || newPassword.isEmpty()) {
                handleError(request, response, "新密码不能为空", false);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                handleError(request, response, "两次输入的新密码不一致", false);
                return;
            }

            if (newPassword.length() < 6) {
                handleError(request, response, "新密码长度不能少于6位", false);
                return;
            }

            // 2. 调用Service修改密码
            boolean success = userService.updatePassword(currentUser.getId(), oldPassword, newPassword);

            if (success) {
                System.out.println("用户密码修改成功: " + currentUser.getUsername());
                handleSuccess(request, response, "密码修改成功");
            } else {
                handleError(request, response, "密码修改失败", false);
            }

        } catch (RuntimeException e) {
            // 修改失败
            System.out.println("用户密码修改失败: " + e.getMessage());
            handleError(request, response, e.getMessage(), false);
        }
    }

    //检查是否为AJax请求
    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    /**
     * 发送JSON响应
     */
    private void sendJsonResponse(HttpServletResponse response, boolean success, String message)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = String.format("{\"success\": %s, \"message\": \"%s\"}",
                success, message);
        response.getWriter().write(json);
    }


    //处理错误响应

    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             String errorMessage, boolean isAjax) throws ServletException, IOException {
        if (isAjax || isAjaxRequest(request)) {
            sendJsonResponse(response, false, errorMessage);
        } else {
            request.setAttribute("error", errorMessage);
            request.getRequestDispatcher("/change-password.jsp").forward(request, response);
        }
    }

    /**
     * 处理成功响应
     */
    private void handleSuccess(HttpServletRequest request, HttpServletResponse response,
                               String successMessage) throws ServletException, IOException {
        if (isAjaxRequest(request)) {
            sendJsonResponse(response, true, successMessage);
        } else {
            request.setAttribute("message", successMessage);
            request.getRequestDispatcher("/change-password.jsp").forward(request, response);//转发到修改密码页面
        }
    }

    /**
     * 用户注销
     */
    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        System.out.println("用户注销");

        // 1. 获取Session并使其失效
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 2. 重定向到登录页面
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}