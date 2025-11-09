<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>修改密码 - 回声网络</title>
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
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .container {
            max-width: 500px;
            width: 100%;
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

        .error-message {
            color: #e74c3c;
            font-size: 0.9em;
            margin-top: 5px;
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
            width: 100%;
        }

        .btn:hover {
            background: #2575fc;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(37, 117, 252, 0.3);
        }

        .btn:disabled {
            background: #b8b8b8;
            cursor: not-allowed;
            transform: none;
            box-shadow: none;
        }

        .message {
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            text-align: center;
            display: none;
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
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>修改密码</h1>
        <p>请设置一个新密码来保护您的账户安全</p >
    </div>

    <!-- 显示后端返回的消息 -->
    <c:if test="${not empty message}">
    <div id="messageDiv" class="message success">${message}</div>
    </c:if>
    <c:if test="${not empty error}">
    <div id="messageDiv" class="message error">${error}</div>
    </c:if>

    <!-- 如果没有后端消息，创建一个空的隐藏消息容器 -->
    <c:if test="${empty message and empty error}">
    <div id="messageDiv" class="message" style="display: none;"></div>
    </c:if>

    <!-- 修复：使用正确的action路径 -->
    <form id="passwordForm" method="post" action="${pageContext.request.contextPath}/user/password">
        <div class="form-group">
            <label for="oldPassword">当前密码</label>
            <input type="password" id="oldPassword" name="oldPassword" class="form-control" placeholder="请输入当前密码" required>
            <span id="oldPasswordError" class="error-message"></span>
        </div>

        <div class="form-group">
            <label for="newPassword">新密码</label>
            <input type="password" id="newPassword" name="newPassword" class="form-control" placeholder="请输入新密码" required>
            <span id="newPasswordError" class="error-message"></span>
        </div>

        <div class="form-group">
            <label for="confirmPassword">确认新密码</label>
            <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" placeholder="请再次输入新密码" required>
            <span id="confirmPasswordError" class="error-message"></span>
        </div>

     <a href="${pageContext.request.contextPath}/index.jsp" >  <button type="submit" id="submitBtn" class="btn">修改密码</button></a>
    </form>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const form = document.getElementById('passwordForm');
        const oldPasswordInput = document.getElementById('oldPassword');
        const newPasswordInput = document.getElementById('newPassword');
        const confirmPasswordInput = document.getElementById('confirmPassword');
        const submitBtn = document.getElementById('submitBtn');
        const oldPasswordError = document.getElementById('oldPasswordError');
        const newPasswordError = document.getElementById('newPasswordError');
        const confirmPasswordError = document.getElementById('confirmPasswordError');
        const messageDiv = document.getElementById('messageDiv');

        // 显示消息函数
        function showMessage(text, type) {
            messageDiv.textContent = text;
            messageDiv.className = `message ${type}`;
            messageDiv.style.display = 'block';

            // 3秒后自动隐藏消息
            setTimeout(() => {
                messageDiv.style.display = 'none';
            }, 3000);
        }        // 表单验证函数
        function validateForm() {
            const oldPassword = oldPasswordInput.value;
            const newPassword = newPasswordInput.value;
            const confirmPassword = confirmPasswordInput.value;

            // 清除之前的错误信息
            oldPasswordError.textContent = '';
            newPasswordError.textContent = '';
            confirmPasswordError.textContent = '';

            let isValid = true;

            // 验证旧密码
            if (!oldPassword) {
                oldPasswordError.textContent = '请输入当前密码';
                isValid = false;
            }

            // 验证新密码
            if (!newPassword) {
                newPasswordError.textContent = '请输入新密码';
                isValid = false;
            } else if (newPassword.length < 6) {
                newPasswordError.textContent = '密码长度至少6个字符';
                isValid = false;
            }

            // 验证确认密码
            if (!confirmPassword) {
                confirmPasswordError.textContent = '请确认新密码';
                isValid = false;
            } else if (newPassword !== confirmPassword) {
                confirmPasswordError.textContent = '两次输入的密码不一致';
                isValid = false;
            }

            return isValid;
        }

        // 表单提交处理
        form.addEventListener('submit', function(e) {
            e.preventDefault();

            if (!validateForm()) {
                return;
            }

            // 禁用提交按钮防止重复提交
            submitBtn.disabled = true;
            submitBtn.textContent = '修改中...';

            // 创建URL编码的数据
            const formData = new URLSearchParams();
            formData.append('oldPassword', oldPasswordInput.value);
            formData.append('newPassword', newPasswordInput.value);
            formData.append('confirmPassword', confirmPasswordInput.value);

            // 发送AJAX请求
            fetch(form.action, {
                method: 'POST',
                body: formData,
                headers: {
                    'X-Requested-With': 'XMLHttpRequest'
                }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('网络响应异常');
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        showMessage(data.message, 'success');
                        form.reset();
                    } else {
                        showMessage(data.message, 'error');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    showMessage('网络错误，请稍后重试', 'error');
                })
                .finally(() => {
                    // 恢复提交按钮
                    submitBtn.disabled = false;
                    submitBtn.textContent = '修改密码';
                });
        });

        // 实时验证
        oldPasswordInput.addEventListener('input', validateForm);
        newPasswordInput.addEventListener('input', validateForm);
        confirmPasswordInput.addEventListener('input', validateForm);

        // 如果后端有消息，显示它
        if (messageDiv.textContent.trim() !== '') {
            messageDiv.style.display = 'block';
        }
    });
</script>
</body>
</html>