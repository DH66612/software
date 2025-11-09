<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>出错了 - 回声网络</title>
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
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .error-container {
            max-width: 600px;
            width: 100%;
            background: white;
            border-radius: 15px;
            padding: 40px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            text-align: center;
        }

        .error-icon {
            font-size: 4em;
            margin-bottom: 20px;
            color: #dc3545;
        }

        .error-header {
            margin-bottom: 20px;
        }

        .error-header h1 {
            font-size: 2.2em;
            margin-bottom: 10px;
            color: #333;
            font-weight: 700;
        }

        .error-header p {
            color: #666;
            font-size: 1.1em;
        }

        .error-details {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin: 25px 0;
            text-align: left;
            border-left: 4px solid #dc3545;
        }

        .error-details h3 {
            font-size: 1.2em;
            margin-bottom: 10px;
            color: #dc3545;
        }

        .error-message {
            color: #721c24;
            background: #f8d7da;
            padding: 10px 15px;
            border-radius: 5px;
            margin-bottom: 15px;
            font-weight: 500;
        }

        .error-stack {
            font-family: monospace;
            font-size: 0.9em;
            color: #666;
            max-height: 200px;
            overflow-y: auto;
            padding: 10px;
            background: white;
            border-radius: 5px;
            border: 1px solid #e9ecef;
        }

        .action-buttons {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 30px;
        }

        .btn {
            display: inline-block;
            padding: 12px 25px;
            background: #6a11cb;
            color: white;
            text-decoration: none;
            border: none;
            border-radius: 8px;
            font-weight: 600;
            transition: all 0.3s ease;
            cursor: pointer;
            font-size: 16px;
        }

        .btn:hover {
            background: #2575fc;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(37, 117, 252, 0.3);
        }

        .btn-secondary {
            background: #6c757d;
        }

        .btn-secondary:hover {
            background: #5a6268;
        }

        .btn-home {
            background: #28a745;
        }

        .btn-home:hover {
            background: #218838;
        }

        .contact-info {
            margin-top: 25px;
            padding-top: 20px;
            border-top: 1px solid #eaeaea;
            color: #666;
            font-size: 0.9em;
        }

        .contact-info a {
            color: #6a11cb;
            text-decoration: none;
        }

        .contact-info a:hover {
            text-decoration: underline;
        }

        /* 响应式设计 */
        @media (max-width: 768px) {
            .error-container {
                padding: 25px 20px;
            }

            .error-header h1 {
                font-size: 1.8em;
            }

            .action-buttons {
                flex-direction: column;
            }

            .btn {
                width: 100%;
            }
        }

        /* 隐藏/显示堆栈跟踪的切换 */
        .toggle-stack {
            background: none;
            border: none;
            color: #6a11cb;
            cursor: pointer;
            font-size: 0.9em;
            margin-top: 10px;
            text-decoration: underline;
        }

        .stack-hidden {
            display: none;
        }
    </style>
</head>
<body>
<div class="error-container">
    <div class="error-icon">⚠️</div>

    <div class="error-header">
        <h1>抱歉，出错了</h1>
        <p>服务器在处理您的请求时遇到了问题</p >
    </div>

    <div class="error-details">
        <h3>错误信息</h3>
        <div class="error-message">
            <%= request.getAttribute("error") != null ?
                    request.getAttribute("error") ://获取请求信息
                    (exception != null ? exception.getMessage() : "未知错误") %>
        </div>

        <!-- 开发环境下显示详细错误信息 -->
        <% if (exception != null && "true".equals(request.getAttribute("debug"))) { %>
        <button class="toggle-stack" onclick="toggleStackTrace()">显示/隐藏详细错误信息</button>
        <div id="stackTrace" class="error-stack stack-hidden">
            <%
                java.io.PrintWriter pw = new java.io.PrintWriter(out);
                exception.printStackTrace(pw);
            %>
        </div>
        <% } %>
    </div>

    <div class="action-buttons">

        <a href="<%= request.getContextPath() %>/index.jsp" class="btn btn-home">返回首页</a >
        <a href="<%= request.getContextPath() %>/article/list" class="btn">浏览文章</a >
    </div>

    <div class="contact-info">
        <p>如果问题持续存在，请联系 <a href="mailto:support@echo.com">技术支持</a ></p >
        <p>错误代码: <%= response.getStatus() %></p >
    </div>
</div>

<script>
    function toggleStackTrace() {
        const stackTrace = document.getElementById('stackTrace');
        stackTrace.classList.toggle('stack-hidden');
    }
</script>
</body>
</html>