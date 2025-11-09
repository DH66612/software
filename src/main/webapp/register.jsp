<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户注册 - Echo</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            /* 使用与登录页面相同的背景图片 */
            background: url('${pageContext.request.contextPath}/images/register-bg.jpg') no-repeat center center fixed;
            background-size: cover;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
            position: relative;
        }

        /* 添加遮罩层，提高表单可读性 */
        body::before {
            content: '';
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.4);
            z-index: -1;
        }

        .register-container {
            background: white;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
            width: 100%;
            max-width: 450px;
            padding: 40px;
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

        .register-header {
            text-align: center;
            margin-bottom: 30px;
        }

        .register-header h1 {
            color: #333;
            font-size: 28px;
            font-weight: 600;
            margin-bottom: 10px;
        }

        .register-header p {
            color: #666;
            font-size: 14px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            color: #333;
            font-weight: 500;
            font-size: 14px;
        }

        .form-control {
            width: 100%;
            padding: 15px;
            border: 2px solid #e1e5e9;
            border-radius: 8px;
            font-size: 14px;
            transition: all 0.3s ease;
            background-color: #f8f9fa;
        }

        .form-control:focus {
            outline: none;
            border-color: #6a11cb;
            background-color: white;
            box-shadow: 0 0 0 3px rgba(106, 17, 203, 0.1);
        }

        .btn-register {
            width: 100%;
            padding: 15px;
            background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-top: 10px;
        }

        .btn-register:hover {
            transform: translateY(-2px);
            box-shadow: 0 7px 15px rgba(106, 17, 203, 0.3);
        }

        .btn-register:active {
            transform: translateY(0);
        }

        .login-link {
            text-align: center;
            margin-top: 25px;
            color: #666;
            font-size: 14px;
        }

        .login-link a {
            color: #6a11cb;
            text-decoration: none;
            font-weight: 500;
            transition: color 0.2s;
        }

        .login-link a:hover {
            color: #2575fc;
            text-decoration: underline;
        }

        .terms {
            margin-top: 20px;
            text-align: center;
            font-size: 12px;
            color: #888;
            line-height: 1.5;
        }

        .terms a {
            color: #6a11cb;
            text-decoration: none;
        }

        .terms a:hover {
            text-decoration: underline;
        }

        .error-message {
            background-color: #fee;
            border: 1px solid #fcc;
            color: #c33;
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 15px;
            font-size: 14px;
            display: none;
        }

        .success-message {
            background-color: #efe;
            border: 1px solid #cfc;
            color: #363;
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 15px;
            font-size: 14px;
            display: none;
        }

        /* 响应式设计 */
        @media (max-width: 480px) {
            body {
                padding: 15px;
            }

            .register-container {
                padding: 30px 25px;
                border-radius: 10px;
            }
        }
    </style>
</head>
<body>
<div class="register-container">
    <!-- 错误消息显示 -->
    <div class="error-message" id="errorMessage"></div>

    <!-- 成功消息显示 -->
    <div class="success-message" id="successMessage"></div>

    <div class="register-header">
        <h1>创建账户</h1>
        <p>加入我们，开始您的旅程</p>
    </div>

    <form id="registerForm" method="post" action="${pageContext.request.contextPath}/user/register">
        <div class="form-group">
            <label for="username">用户名</label>
            <input type="text" id="username" name="username" class="form-control"
                   placeholder="请输入用户名" required maxlength="20">
        </div>

        <div class="form-group">
            <label for="email">电子邮箱</label>
            <input type="email" id="email" name="email" class="form-control"
                   placeholder="请输入电子邮箱" required>
        </div>

        <div class="form-group">
            <label for="password">密码</label>
            <input type="password" id="password" name="password" class="form-control"
                   placeholder="请输入密码" required minlength="6">
        </div>

        <div class="form-group">
            <label for="confirmPassword">确认密码</label>
            <input type="password" id="confirmPassword" name="confirmPassword" class="form-control"
                   placeholder="请再次输入密码" required minlength="6">
        </div>

        <button type="submit" class="btn-register">注册</button>
    </form>

    <div class="login-link">
        已有账户？<a href="${pageContext.request.contextPath}/user/login">立即登录</a>
    </div>

    <div class="terms">
        注册即表示您同意我们的
        <a href="${pageContext.request.contextPath}/privacy_policy.jsp" target="_blank">隐私政策</a>
        和
        <a href="${pageContext.request.contextPath}/terms_of_service.jsp" target="_blank">服务条款</a>
    </div>
</div>

<script>
    // 表单验证
    document.getElementById('registerForm').addEventListener('submit', function(e) {
        e.preventDefault();

        const username = document.getElementById('username').value.trim();
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const errorMessage = document.getElementById('errorMessage');
        const successMessage = document.getElementById('successMessage');

        // 隐藏之前的消息
        errorMessage.style.display = 'none';
        successMessage.style.display = 'none';

        // 基础验证
        if (!username) {
            showError('请输入用户名');
            return;
        }

        if (username.length < 3 || username.length > 20) {
            showError('用户名长度应在3-20个字符之间');
            return;
        }

        if (!email) {
            showError('请输入电子邮箱');
            return;
        }

        if (!isValidEmail(email)) {
            showError('请输入有效的电子邮箱地址');
            return;
        }

        if (!password) {
            showError('请输入密码');
            return;
        }

        if (password.length < 6) {
            showError('密码长度至少6位');
            return;
        }

        if (password !== confirmPassword) {
            showError('两次输入的密码不一致');
            return;
        }

        // 验证通过，提交表单
        this.submit();
    });

    function isValidEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    function showError(message) {
        const errorMessage = document.getElementById('errorMessage');
        errorMessage.textContent = message;
        errorMessage.style.display = 'block';

        // 自动隐藏错误消息
        setTimeout(() => {
            errorMessage.style.display = 'none';
        }, 5000);
    }

    function showSuccess(message) {
        const successMessage = document.getElementById('successMessage');
        successMessage.textContent = message;
        successMessage.style.display = 'block';
    }

    // 实时密码匹配检查
    document.getElementById('confirmPassword').addEventListener('input', function() {
        const password = document.getElementById('password').value;
        const confirmPassword = this.value;
        const errorMessage = document.getElementById('errorMessage');

        if (confirmPassword && password !== confirmPassword) {
            errorMessage.textContent = '密码不匹配';
            errorMessage.style.display = 'block';
        } else {
            errorMessage.style.display = 'none';
        }
    });

    // 清除错误消息当用户开始输入时
    const inputs = document.querySelectorAll('input');
    inputs.forEach(input => {
        input.addEventListener('input', function() {
            const errorMessage = document.getElementById('errorMessage');
            errorMessage.style.display = 'none';
        });
    });

    // 显示服务器返回的消息（如果有）
    <c:if test="${not empty error}">
    showError('${error}');
    </c:if>

    <c:if test="${not empty success}">
    showSuccess('${success}');
    </c:if>
</script>
</body>
</html>