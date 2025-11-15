<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>回声网络 - 发现好文章</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background: #f8f9fa;
            color: #333;
            line-height: 1.6;
        }

        .header {
            background: white;
            box-shadow: 0 2px 15px rgba(0,0,0,0.1);
            position: fixed;
            width: 100%;
            top: 0;
            z-index: 1000;
        }

        .nav {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            height: 70px;
        }

        .logo {
            font-size: 1.8em;
            font-weight: bold;
            background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            text-decoration: none;
        }

        .nav-links {
            display: flex;
            list-style: none;
        }

        .nav-links li {
            margin-left: 30px;
        }

        .nav-links a {
            text-decoration: none;
            color: #555;
            font-weight: 500;
            transition: color 0.3s;
            padding: 8px 0;
            position: relative;
        }

        .nav-links a:hover {
            color: #6a11cb;
        }

        .nav-links a::after {
            content: '';
            position: absolute;
            width: 0;
            height: 2px;
            bottom: 0;
            left: 0;
            background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
            transition: width 0.3s;
        }

        .nav-links a:hover::after {
            width: 100%;
        }

        /* 用户信息区域 */
        .user-info {
            display: flex;
            align-items: center;
            gap: 12px;
            position: relative;
            cursor: pointer;
        }

        .avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            object-fit: cover;
            border: 2px solid #e1e5ee;
        }

        .user-name {
            font-weight: 500;
            color: #555;
        }

        .dropdown-arrow {
            font-size: 0.8em;
            color: #777;
            transition: transform 0.3s;
        }

        /* 下拉菜单 */
        .dropdown-menu {
            position: absolute;
            top: 100%;
            right: 0;
            background: white;
            border-radius: 8px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            min-width: 180px;
            padding: 10px 0;
            margin-top: 10px;
            opacity: 0;
            visibility: hidden;
            transform: translateY(-10px);
            transition: all 0.3s ease;
            z-index: 1001;
        }

        .dropdown-menu.active {
            opacity: 1;
            visibility: visible;
            transform: translateY(0);
        }

        .dropdown-menu a {
            display: block;
            padding: 12px 20px;
            color: #555;
            text-decoration: none;
            transition: all 0.2s;
            font-weight: 500;
        }

        .dropdown-menu a:hover {
            background: #f8f9fa;
            color: #6a11cb;
        }

        .dropdown-divider {
            height: 1px;
            background: #eaeaea;
            margin: 8px 0;
        }

        /* 登录/注册按钮 */
        .auth-buttons {
            display: flex;
            gap: 15px;
        }

        .auth-btn {
            padding: 10px 20px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: 500;
            transition: all 0.3s;
        }

        .auth-btn.login {
            color: #6a11cb;
            border: 1px solid #6a11cb;
        }

        .auth-btn.login:hover {
            background: #6a11cb;
            color: white;
        }

        .auth-btn.register {
            background: #6a11cb;
            color: white;
        }

        .auth-btn.register:hover {
            background: #2575fc;
            transform: translateY(-2px);
        }

        .container {
            max-width: 1200px;
            margin: 90px auto 40px;
            padding: 0 20px;
        }

        .hero {
            background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
            color: white;
            padding: 80px 40px;
            border-radius: 20px;
            text-align: center;
            margin-bottom: 50px;
            box-shadow: 0 10px 30px rgba(106, 17, 203, 0.3);
        }

        .hero h1 {
            font-size: 3em;
            margin-bottom: 20px;
            font-weight: 700;
        }

        .hero p {
            font-size: 1.3em;
            opacity: 0.9;
            margin-bottom: 40px;
            max-width: 600px;
            margin-left: auto;
            margin-right: auto;
        }

        .btn {
            display: inline-block;
            padding: 15px 35px;
            background: white;
            color: #6a11cb;
            text-decoration: none;
            border-radius: 50px;
            font-weight: 600;
            transition: all 0.3s ease;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }

        .btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 20px rgba(0,0,0,0.15);
        }

        .section-title {
            font-size: 1.8em;
            margin: 40px 0 25px;
            color: #333;
            text-align: center;
            font-weight: 600;
        }

        .articles-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
            gap: 30px;
            margin-bottom: 50px;
        }

        .no-articles {
            grid-column: 1 / -1;
            text-align: center;
            padding: 60px 20px;
            background: white;
            border-radius: 15px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
        }

        .no-articles h3 {
            font-size: 1.5em;
            margin-bottom: 15px;
            color: #666;
        }

        .no-articles p {
            color: #888;
            margin-bottom: 25px;
        }

        .footer {
            text-align: center;
            padding: 50px 20px;
            color: #666;
            border-top: 1px solid #eee;
            margin-top: 60px;
            background: white;
        }

        .footer-links {
            margin-top: 20px;
        }

        .footer-links a {
            color: #666;
            text-decoration: none;
            margin: 0 15px;
            transition: color 0.3s;
        }

        .footer-links a:hover {
            color: #6a11cb;
        }

        .load-more {
            text-align: center;
            margin: 40px 0;
        }

        .btn-secondary {
            background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
            color: white;
            padding: 12px 30px;
        }

        @media (max-width: 768px) {
            .nav {
                height: 60px;
            }

            .nav-links {
                display: none;
            }

            .articles-grid {
                grid-template-columns: 1fr;
            }

            .hero {
                padding: 60px 20px;
            }

            .hero h1 {
                font-size: 2.2em;
            }

            .hero p {
                font-size: 1.1em;
            }

            .container {
                margin-top: 70px;
            }

            .auth-buttons {
                gap: 10px;
            }

            .auth-btn {
                padding: 8px 15px;
                font-size: 0.9em;
            }
        }
    </style>
</head>
<body>
<header class="header">
    <nav class="nav">
        <a href="#" class="logo">回声网络</a >
        <ul class="nav-links">
            <li><a href="${pageContext.request.contextPath}/index.jsp">首页</a ></li>
            <li><a href="${pageContext.request.contextPath}/article/list">发现</a ></li>
            <li><a href="#">关于</a ></li>
        </ul>


        <% if (session.getAttribute("currentUser") != null) { %>

        <div class="user-info" id="userDropdown">
            <img src="${pageContext.request.contextPath}${not empty sessionScope.currentUser.avatar ? sessionScope.currentUser.avatar : '/images/avatar-default.png'}"
                 alt="用户头像" class="avatar">
            <span class="user-name">${sessionScope.currentUser.username}</span>
            <span class="dropdown-arrow">▼</span>

            <div class="dropdown-menu" id="dropdownMenu">
                <a href="${pageContext.request.contextPath}/user/profile">个人中心</a >
                <a href="${pageContext.request.contextPath}/change-password.jsp">修改密码</a >
                <a href="${pageContext.request.contextPath}/publish-article.jsp">发布文章</a >
                <div class="dropdown-divider"></div>
                <a href="${pageContext.request.contextPath}/user/logout">退出登录</a >
            </div>
        </div>
        <% } else { %>

        <div class="auth-buttons">
            <a href="${pageContext.request.contextPath}/user/login" class="auth-btn login">登录</a >
            <a href="${pageContext.request.contextPath}/user/register" class="auth-btn register">注册</a >
        </div>
        <% } %>
    </nav>
</header>
<div class="container">
    <section class="hero">
        <h1>分享你的想法，发现精彩内容</h1>
        <p>在回声网络，每个人都可以成为创作者，分享你的知识和见解</p >
        <a href="${pageContext.request.contextPath}/article/publish" class="btn">开始写作</a >
    </section>



    <div class="articles-grid">

        <div class="no-articles">
            <h3>写作与阅读的奇妙之处</h3>
            <p>写作能够帮助我们整理思绪、深化思考，将模糊的想法转化为清晰的文字。<br>
                阅读则让我们跨越时空界限，与智者对话，汲取智慧精华。<br>
                在这里，你可以自由表达，也可以从他人的文字中获得启发与共鸣。<br>
                开始你的创作之旅，让思想在文字中绽放光彩！</p >

        </div>
    </div>

    <div class="load-more">
        <a href="${pageContext.request.contextPath}/article/list" class="btn btn-secondary">浏览更多文章</a >
    </div>
</div>

<footer class="footer">
    <p>回声网络 © 2025 - 让每个人都能发出自己的声音</p >
    <div class="footer-links">
        <a href="${pageContext.request.contextPath}/terms_of_service.jsp">服务条款</a >
        <a href="${pageContext.request.contextPath}/privacy_policy.jsp">隐私政策</a >
        <a href="#">联系我们</a >
    </div>
</footer>

<script>

    document.addEventListener('DOMContentLoaded', function() {
        const userDropdown = document.getElementById('userDropdown');
        const dropdownMenu = document.getElementById('dropdownMenu');

        if (userDropdown && dropdownMenu) {

            userDropdown.addEventListener('click', function(e) {
                e.stopPropagation();
                dropdownMenu.classList.toggle('active');


                const arrow = this.querySelector('.dropdown-arrow');
                arrow.style.transform = dropdownMenu.classList.contains('active') ? 'rotate(180deg)' : 'rotate(0)';
            });


            document.addEventListener('click', function() {
                dropdownMenu.classList.remove('active');
                const arrow = userDropdown.querySelector('.dropdown-arrow');
                arrow.style.transform = 'rotate(0)';
            });


            dropdownMenu.addEventListener('click', function(e) {
                e.stopPropagation();
            });
        }
    });
</script>
</body>
</html>