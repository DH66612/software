<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人资料 - 回声网络</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            color: #333;
            line-height: 1.6;
            padding: 20px;
            min-height: 100vh;
        }

        .container {
            max-width: 600px;
            margin: 0 auto;
            background: white;
            border-radius: 15px;
            padding: 40px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        }

        .header {
            text-align: center;
            margin-bottom: 40px;
            padding-bottom: 20px;
            border-bottom: 1px solid #eaeaea;
        }

        .header h1 {
            font-size: 2.2em;
            margin-bottom: 10px;
            color: #333;
            font-weight: 700;
        }

        .header p {
            color: #666;
            font-size: 1.1em;
        }

        .profile-section {
            margin-bottom: 30px;
        }

        .profile-avatar {
            text-align: center;
            margin-bottom: 30px;
        }

        .avatar {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            object-fit: cover;
            border: 4px solid #eaeaea;
            margin-bottom: 15px;
        }

        .change-avatar {
            color: #6a11cb;
            text-decoration: none;
            font-weight: 500;
            cursor: pointer;
        }

        .form-group {
            margin-bottom: 25px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 500;
            color: #555;
            font-size: 14px;
        }

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

        .form-control:disabled {
            background-color: #f5f5f5;
            color: #999;
            cursor: not-allowed;
        }

        .btn {
            display: inline-block;
            padding: 15px 30px;
            background: #6a11cb;
            color: white;
            text-decoration: none;
            border: none;
            border-radius: 10px;
            font-weight: 600;
            transition: all 0.3s ease;
            cursor: pointer;
            font-size: 16px;
            text-align: center;
        }

        .btn:hover {
            background: #2575fc;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(37, 117, 252, 0.3);
        }

        .btn-full {
            width: 100%;
        }

        .btn-secondary {
            background: #6c757d;
        }

        .btn-secondary:hover {
            background: #5a6268;
        }

        .btn-articles {
            background: #28a745;
        }

        .btn-articles:hover {
            background: #218838;
        }

        .action-buttons {
            display: flex;
            flex-direction: column;
            gap: 15px;
            margin-top: 30px;
        }

        .action-row {
            display: flex;
            gap: 15px;
        }

        .action-row .btn {
            flex: 1;
        }

        .message {
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            text-align: center;
        }

        .message.success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .message.error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        @media (max-width: 768px) {
            .container {
                padding: 25px 20px;
            }

            .header h1 {
                font-size: 1.8em;
            }

            .action-row {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <a href="${pageContext.request.contextPath}/index.jsp"><h1>回声网络</h1></a >
        <p>管理您的账户信息和偏好设置</p >
    </div>

    <!-- 修正消息显示 -->
    <% if (request.getAttribute("message") != null) { %>
    <div class="message success">
        <%= request.getAttribute("message") %>
    </div>
    <% } %>

    <% if (request.getAttribute("error") != null) { %>
    <div class="message error">
        <%= request.getAttribute("error") %>
    </div>
    <% } %>

    <!-- 修正头像显示部分 -->
    <div class="profile-avatar">
        <form action="${pageContext.request.contextPath}/user/uploadAvatar" method="post" enctype="multipart/form-data" id="avatarForm">
            <!-- 修正这里：确保头像URL正确获取 -->
            <img src="${pageContext.request.contextPath}${not empty sessionScope.currentUser.avatar ? sessionScope.currentUser.avatar : '/images/avatar-default.png'}"
                 alt="头像" class="avatar" id="avatarImage">
            <div style="margin-top: 15px;">
                <input type="file" name="avatar" id="avatarInput" accept="image/jpeg,image/png,image/gif" style="display: none;">
                <button type="button" class="btn" onclick="document.getElementById('avatarInput').click()" style="margin-right: 10px;">
                    选择图片
                </button>
                <button type="submit" class="btn btn-secondary" id="uploadBtn" style="display: none;">
                    上传头像
                </button>
            </div>
            <div style="margin-top: 10px; font-size: 0.9em; color: #666;">
                支持 JPG、PNG、GIF 格式，大小不超过 5MB
            </div>
        </form>
    </div>

    <!-- 主要信息修改表单 -->
    <form action="${pageContext.request.contextPath}/user/update" method="POST" id="profileForm">
        <div class="form-group">
            <label for="username">用户名</label>
            <input type="text" id="username" name="username" class="form-control"
                   value="${sessionScope.currentUser.username}" readonly>
            <small style="color: #666; font-size: 0.9em;">用户名创建后不可修改</small>
        </div>

        <div class="form-group">
            <label for="email">邮箱地址</label>
            <input type="email" id="email" name="email" class="form-control"
                   value="${sessionScope.currentUser.email}" required>
        </div>

        <div class="form-group">
            <label for="nickname">昵称</label>
            <input type="text" id="nickname" name="nickname" class="form-control"
                   value="${sessionScope.currentUser.nickname != null ? sessionScope.currentUser.nickname : ''}"
                   placeholder="请输入您的昵称">
        </div>

        <div class="action-buttons">
            <div class="action-row">
                <button type="submit" class="btn">保存修改</button>
                <a href="${pageContext.request.contextPath}/user/password" class="btn btn-secondary">修改密码</a >
            </div>
            <a href="${pageContext.request.contextPath}/article/my-articles" class="btn btn-articles btn-full">我的文章</a >
        </div>
    </form>
</div>

<script>
    // 头像上传预览
    document.getElementById('avatarInput').addEventListener('change', function(e) {
        const file = e.target.files[0];
        const uploadBtn = document.getElementById('uploadBtn');

        if (file) {
            // 文件类型验证
            const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
            if (!allowedTypes.includes(file.type)) {
                alert('请选择 JPG、PNG 或 GIF 格式的图片！');
                this.value = '';
                uploadBtn.style.display = 'none';
                return;
            }

            // 文件大小验证（限制为5MB）
            if (file.size > 5 * 1024 * 1024) {
                alert('图片大小不能超过5MB！');
                this.value = '';
                uploadBtn.style.display = 'none';
                return;
            }

            // 预览图片
            const reader = new FileReader();
            reader.onload = function(e) {
                document.getElementById('avatarImage').src = e.target.result;
                // 显示上传按钮
                uploadBtn.style.display = 'inline-block';
            }
            reader.readAsDataURL(file);
        } else {
            uploadBtn.style.display = 'none';
        }
    });

    // 头像表单提交
    document.getElementById('avatarForm').addEventListener('submit', function(e) {
        const fileInput = document.getElementById('avatarInput');
        if (!fileInput.files[0]) {
            e.preventDefault();
            alert('请先选择要上传的图片！');
            return;
        }

        // 显示上传中状态
        const uploadBtn = document.getElementById('uploadBtn');
        uploadBtn.innerHTML = '上传中...';
        uploadBtn.disabled = true;
    });

    // 个人信息表单使用AJAX提交
    document.getElementById('profileForm').addEventListener('submit', function(e) {
        e.preventDefault();

        const email = document.getElementById('email').value;
        const nickname = document.getElementById('nickname').value;

        // 验证逻辑...
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            alert('请输入有效的邮箱地址！');
            return;
        }

        if (nickname.length > 20) {
            alert('昵称长度不能超过20个字符！');
            return;
        }

        // 使用AJAX提交表单
        const formData = new FormData(this);

        fetch(this.action, {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url;
                } else {
                    return response.text();
                }
            })
            .then(data => {
                // 显示成功消息，但不刷新页面
                showMessage('个人信息更新成功', 'success');
            })
            .catch(error => {
                console.error('Error:', error);
                showMessage('更新失败，请重试', 'error');
            });
    });

    // 显示消息的函数
    function showMessage(message, type) {
        // 移除现有消息
        const existingMessage = document.querySelector('.message');
        if (existingMessage) {
            existingMessage.remove();
        }

        // 创建新消息
        const messageDiv = document.createElement('div');
        messageDiv.className = `message ${type}`;
        messageDiv.textContent = message;

        // 插入到容器开头
        const container = document.querySelector('.container');
        const header = document.querySelector('.header');
        container.insertBefore(messageDiv, header.nextSibling);

        // 3秒后自动消失
        setTimeout(() => {
            messageDiv.remove();
        }, 3000);
    }
</script>
</body>
</html>