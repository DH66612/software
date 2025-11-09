<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>博客系统 - 登录</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background: url('${pageContext.request.contextPath}/images/login-bg.jpg') no-repeat center center fixed;
            background-size: cover;
            /* 添加以下居中对齐样式 */
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            padding: 20px;
        }

        .login-container {
            background: white;
            border-radius: 15px;
            box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
            width: 100%;
            max-width: 420px;
            overflow: hidden;
            /* 添加动画效果（可选） */
            animation: fadeInUp 0.6s ease-out;
        }

        /* 添加淡入动画 */
        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .login-header {
            background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
            color: white;
            padding: 30px 20px;
            text-align: center;
        }

        .login-header h1 {
            font-size: 28px;
            margin-bottom: 8px;
            font-weight: 600;
        }

        .login-header p {
            opacity: 0.9;
            font-size: 15px;
        }

        .login-form {
            padding: 30px;
        }

        .form-group {
            margin-bottom: 25px;
            position: relative;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 500;
            color: #555;
            font-size: 14px;
        }

        /* 美化输入框 */
        .form-control {
            width: 100%;
            padding: 15px 20px;
            border: 2px solid #e1e5ee;
            border-radius: 10px;
            font-size: 16px;
            transition: all 0.3s ease;
            background-color: #f8f9fa;
            outline: none;
        }

        .form-control:focus {
            border-color: #6a11cb;
            background-color: white;
            box-shadow: 0 0 0 3px rgba(106, 17, 203, 0.1);
        }

        .form-control:hover {
            border-color: #b8b9c5;
        }

        .error-message {
            color: #e74c3c;
            font-size: 13px;
            margin-top: 6px;
            display: flex;
            align-items: center;
        }

        .error-message::before {
            content: "!";
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 16px;
            height: 16px;
            background: #e74c3c;
            color: white;
            border-radius: 50%;
            font-size: 12px;
            margin-right: 6px;
        }

        .btn-login {
            width: 100%;
            padding: 15px;
            background: linear-gradient(135deg, #3b8ffb 0%, #79a7f4 100%);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-top: 10px;
        }

        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 7px 15px rgba(106, 17, 203, 0.3);
        }

        .btn-login:active {
            transform: translateY(0);
        }

        .signup-link {
            text-align: center;
            margin-top: 25px;
            color: #666;
            font-size: 14px;
        }

        .signup-link a {
            color: #6a11cb;
            text-decoration: none;
            font-weight: 500;
            transition: color 0.2s;
        }

        .signup-link a:hover {
            color: #2575fc;
            text-decoration: underline;
        }

        /* 响应式设计 */
        @media (max-width: 480px) {
            body {
                padding: 15px;
            }

            .login-container {
                border-radius: 10px;
            }

            .login-header {
                padding: 25px 15px;
            }

            .login-form {
                padding: 25px 20px;
            }
        }
    </style>
</head>
<body>
<div class="login-container">
    <div class="login-header">
        <h1>博客系统</h1>
        <p>登录您的账户，开始创作与分享</p>
    </div>

    <!-- 服务器错误信息 -->
    <% if (request.getAttribute("error") != null) { %>
    <div class="server-error" style="color: red; background: #ffe6e6; padding: 10px; border-radius: 4px; margin-bottom: 15px;">
        <%= request.getAttribute("error") %>
    </div>
    <% } %>

    <form class="login-form" action="${pageContext.request.contextPath}/user/login" method="POST">
        <!-- 重定向到文章列表 -->
        <input type="hidden" name="redirect" value="${pageContext.request.contextPath}/article/list">

        <div class="form-group">
            <label for="username">用户名</label>
            <input type="text" id="username" name="username" class="form-control"
                   placeholder="请输入用户名"
                   value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>">
            <div class="error-message" id="usernameError"></div>
        </div>

        <div class="form-group">
            <label for="password">密码</label>
            <input type="password" id="password" name="password" class="form-control" placeholder="请输入密码">
            <div class="error-message" id="passwordError"></div>
        </div>

        <button type="submit" class="btn-login">登录</button>

        <div class="signup-link">
            还没有账户? <a href="${pageContext.request.contextPath}/user/register">立即注册</a>
        </div>
    </form>
</div>

<script>
    document.querySelector('.login-form').addEventListener('submit', function(e) {
        let isValid = true;
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value.trim();

        // 清除之前的错误信息
        document.getElementById('usernameError').textContent = '';
        document.getElementById('passwordError').textContent = '';

        if (!username) {
            document.getElementById('usernameError').textContent = '用户名不能为空';
            isValid = false;
        }

        if (!password) {
            document.getElementById('passwordError').textContent = '密码不能为空';
            isValid = false;
        }

        if (!isValid) {
            e.preventDefault();
        }
    });
</script>
</body>
</html>