//上传头像时用的
package com.echo.controller;

import com.echo.entity.User;
import com.echo.service.UserService;
import com.echo.service.UserServiceImpl;
import com.echo.utils.SessionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
//web映射
@WebServlet("/user/uploadAvatar")
public class UploadAvatarServlet extends HttpServlet {

    private UserService userService;
    private String uploadPath;
//进行初始化
    @Override
    public void init() throws ServletException {
        this.userService = new UserServiceImpl();
        // 修正上传路径 - 确保路径正确
        String contextPath = getServletContext().getContextPath();
        this.uploadPath = getServletContext().getRealPath("/") + "uploads/avatars/";

        System.out.println("上传路径: " + uploadPath);
        System.out.println("上下文路径: " + contextPath);

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            System.out.println("创建上传目录: " + created + ", 路径: " + uploadDir.getAbsolutePath());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== 开始处理头像上传 ===");

        // 检查用户是否登录
        User currentUser = SessionUtils.getCurrentUser(request);
        if (currentUser == null) {
            System.out.println("用户未登录");
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }

        System.out.println("当前用户: " + currentUser.getUsername());

        // 检查是否为multipart/form-data
        if (!ServletFileUpload.isMultipartContent(request)) {
            System.out.println("表单不是multipart类型");
            request.setAttribute("error", "表单必须包含文件上传");
            response.sendRedirect(request.getContextPath() + "/user/profile");
            return;
        }

        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 ，超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(1024 * 1024); // 1MB
        // 设置临时存储目录
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        factory.setRepository(tempDir);

        ServletFileUpload upload = new ServletFileUpload(factory);

        // 设置最大文件上传值
        upload.setFileSizeMax(1024 * 1024 * 5); // 5MB
        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(1024 * 1024 * 10); // 10MB

        try {
            List<FileItem> items = upload.parseRequest(request);
            String avatarUrl = null;

            System.out.println("解析到 " + items.size() + " 个表单项");

            for (FileItem item : items) {
                if (!item.isFormField() && "avatar".equals(item.getFieldName())) {
                    // 处理文件上传
                    String fileName = item.getName();
                    System.out.println("上传文件: " + fileName);

                    if (fileName != null && !fileName.isEmpty()) {
                        // 验证文件类型
                        String contentType = item.getContentType();
                        if (!contentType.startsWith("image/")) {
                            request.setAttribute("error", "只能上传图片文件");
                            response.sendRedirect(request.getContextPath() + "/user/profile");//重定向
                            return;
                        }

                        // 生成唯一文件名
                        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
                        String newFileName = UUID.randomUUID().toString() + fileExtension;

                        // 保存文件
                        File storeFile = new File(uploadPath + newFileName);
                        System.out.println("保存文件到: " + storeFile.getAbsolutePath());

                        item.write(storeFile);

                        // 设置头像URL - 使用相对路径
                        avatarUrl = "/uploads/avatars/" + newFileName;
                        System.out.println("头像URL: " + avatarUrl);
                    }
                    break;
                }
            }

            if (avatarUrl != null) {
                // 更新数据库中的头像URL
                boolean success = userService.updateAvatar(currentUser.getId(), avatarUrl);
                if (success) {
                    // 更新session中的用户信息
                    currentUser.setAvatar(avatarUrl);
                    SessionUtils.setCurrentUser(request, currentUser);
                    request.getSession().setAttribute("message", "头像更新成功");
                    System.out.println("头像更新成功");
                } else {
                    request.getSession().setAttribute("error", "头像更新失败");
                    System.out.println("数据库更新失败");
                }
            } else {
                request.getSession().setAttribute("error", "请选择要上传的头像文件");
                System.out.println("未选择文件");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "文件上传失败: " + e.getMessage());
            System.out.println("上传异常: " + e.getMessage());
        }

        // 重定向回个人资料页面
        response.sendRedirect(request.getContextPath() + "/user/profile");
    }
}