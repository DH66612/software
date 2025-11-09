<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>回声网络 - 隐私政策</title>
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
            max-width: 900px;
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

        h2 {
            color: #2c3e50;
            margin: 30px 0 15px;
            padding-bottom: 10px;
            border-bottom: 1px solid #eaeaea;
            font-weight: 600;
        }

        h3 {
            color: #34495e;
            margin: 25px 0 10px;
            font-weight: 500;
        }

        p {
            margin-bottom: 15px;
            text-align: justify;
            color: #555;
        }

        .highlight {
            background: #f8f9fa;
            padding: 15px 20px;
            border-radius: 8px;
            border-left: 4px solid #6a11cb;
            margin: 20px 0;
        }

        .warning {
            background: #fff3e0;
            padding: 15px 20px;
            border-radius: 8px;
            border-left: 4px solid #ff9800;
            margin: 20px 0;
        }

        ul {
            margin: 15px 0;
            padding-left: 25px;
        }

        li {
            margin-bottom: 8px;
            color: #555;
        }

        .footer {
            text-align: center;
            margin-top: 40px;
            padding-top: 30px;
            border-top: 1px solid #eaeaea;
            color: #666;
        }

        .nav-buttons {
            display: flex;
            justify-content: space-between;
            margin-top: 30px;
        }

        .nav-btn {
            padding: 12px 25px;
            background: #6a11cb;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
            font-weight: 500;
        }

        .nav-btn:hover {
            background: #2575fc;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(37, 117, 252, 0.3);
        }

        .small-print {
            font-size: 0.85em;
            color: #888;
            text-align: center;
            margin-top: 10px;
        }

        .section {
            margin-bottom: 30px;
        }

        .data-table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 0 10px rgba(0,0,0,0.05);
        }

        .data-table th, .data-table td {
            border: 1px solid #eaeaea;
            padding: 12px 15px;
            text-align: left;
        }

        .data-table th {
            background-color: #f8f9fa;
            font-weight: 600;
            color: #333;
        }

        .data-table tr:nth-child(even) {
            background-color: #f8f9fa;
        }

        @media (max-width: 768px) {
            .container {
                padding: 25px 20px;
            }

            .header h1 {
                font-size: 1.8em;
            }

            .nav-buttons {
                flex-direction: column;
                gap: 15px;
            }

            .nav-btn {
                text-align: center;
            }

            .data-table {
                display: block;
                overflow-x: auto;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>回声网络隐私政策</h1>
        <p>保护您的隐私是我们的首要任务</p >
    </div>

    <div class="highlight">
        <strong>核心承诺：</strong>我们收集尽可能少的数据，保护我们收集的数据，并且绝不会出售您的个人数据。
    </div>

    <div class="section">
        <h2>1. 信息收集</h2>
        <p>我们只收集使回声网络正常运行所必需的信息：</p >

        <table class="data-table">
            <tr>
                <th>信息类型</th>
                <th>为什么需要</th>
                <th>是否必需</th>
            </tr>
            <tr>
                <td>账号信息（用户名、邮箱）</td>
                <td>创建您的账号，让您登录</td>
                <td>是的</td>
            </tr>
            <tr>
                <td>个人资料（昵称、头像）</td>
                <td>个性化您的体验</td>
                <td>可选的</td>
            </tr>
            <tr>
                <td>您发布的内容</td>
                <td>让回声网络有内容可显示</td>
                <td>是的（如果您想发布内容）</td>
            </tr>
            <tr>
                <td>技术信息（IP地址、浏览器类型）</td>
                <td>确保网站安全和正常运行</td>
                <td>自动收集</td>
            </tr>
        </table>
    </div>

    <div class="section">
        <h2>2. 我们不收集的信息</h2>
        <p>我们坚决不收集以下信息：</p >
        <ul>
            <li>您的确切地理位置（除非您主动提供）</li>
            <li>您的联系人列表</li>
            <li>您的银行账户信息</li>
            <li>您的生物特征数据（指纹、面部识别等）</li>
        </ul>
    </div>

    <div class="section">
        <h2>3. 信息使用</h2>
        <p>我们使用您的信息来：</p >
        <ul>
            <li>提供、维护和改进回声网络服务</li>
            <li>创建和管理您的账号</li>
            <li>与您沟通（比如服务更新或安全通知）</li>
            <li>个性化您的体验</li>
            <li>确保服务安全和防止滥用</li>
        </ul>
    </div>

    <div class="warning">
        <strong>数据共享：</strong>我们不会将您的个人数据出售给第三方。我们只会在以下情况下共享数据：(1) 征得您的同意；(2) 为了提供您要求的服务；(3) 法律要求。
    </div>

    <div class="section">
        <h2>4. Cookie和技术</h2>
        <p>像大多数网站一样，我们使用Cookie和类似技术：</p >
        <ul>
            <li><strong>认证Cookie：</strong>记住您的登录状态，这样您就不需要每次都登录</li>
            <li><strong>偏好Cookie：</strong>记住您的设置（比如主题偏好）</li>
            <li><strong>分析Cookie：</strong>帮助我们了解人们如何使用回声网络，以便我们改进</li>
        </ul>
        <p>您可以通过浏览器设置控制Cookie，但禁用某些Cookie可能会影响网站功能。</p >
    </div><div class="section">
    <h2>5. 数据安全</h2>
    <p>我们采取合理措施保护您的数据：</p >
    <ul>
        <li>使用加密技术保护数据传输</li>
        <li>定期更新我们的系统和软件</li>
        <li>限制员工访问数据的权限</li>
        <li>定期进行安全评估</li>
    </ul>
</div>

    <div class="section">
        <h2>6. 数据存储和传输</h2>
        <p>您的数据主要存储在我们位于中国的服务器上。当我们使用位于其他国家的服务提供商时，我们会确保它们提供足够的数据保护。</p >
    </div>

    <div class="section">
        <h2>7. 数据保留</h2>
        <p>我们只会在必要时保留您的数据：</p >
        <ul>
            <li>只要您的账号处于活动状态，我们就会保留您的账号数据</li>
            <li>您发布的内容在您删除前会一直保留</li>
            <li>当您删除账号时，我们会删除或匿名化您的个人数据</li>
        </ul>
    </div>

    <div class="section">
        <h2>8. 您的权利</h2>
        <p>您对您的数据拥有以下权利：</p >
        <ul>
            <li>访问您的个人数据</li>
            <li>更正不准确的数据</li>
            <li>删除您的个人数据</li>
            <li>限制或反对某些数据处理</li>
            <li>获取您的数据副本</li>
        </ul>
    </div>

    <div class="section">
        <h2>9. 联系我们</h2>
        <p>如果您对隐私政策有任何疑问，或想行使您的数据权利：</p >
        <ul>
            <li>邮箱：privacy@echo.com</li>
            <li>我们承诺在30天内回复所有隐私相关查询</li>
        </ul>
    </div>

    <div class="nav-buttons">
        <a href="${pageContext.request.contextPath}/terms_of_service.jsp" class="nav-btn">查看服务条款</a >
        <a href="${pageContext.request.contextPath}/index.jsp" class="nav-btn">返回首页</a >
    </div>

    <div class="footer">
        <p>回声网络 - 让每个人都能发出自己的声音</p >
        <p class="small-print">
            最后更新：2024年1月<br>
            本隐私政策可能会不时更新，请定期查看
        </p >
    </div>
</div>
</body>
</html>