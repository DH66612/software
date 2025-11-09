<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的文章 - 回声网络</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .articles-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        .page-header {
            margin-bottom: 40px;
            padding-bottom: 20px;
            border-bottom: none;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 12px;
            padding: 30px;
            color: white;
            text-align: center;
            box-shadow: 0 8px 25px rgba(0,0,0,0.1);
        }

        .page-title {
            font-size: 32px;
            font-weight: 700;
            margin: 0 0 10px 0;
            color: white;
        }

        .page-header p {
            font-size: 16px;
            opacity: 0.9;
            margin: 0;
        }

        .article-card {
            background: #fff;
            border: none;
            border-radius: 16px;
            padding: 25px;
            margin-bottom: 25px;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            box-shadow: 0 2px 12px rgba(0,0,0,0.08);
            border-left: 4px solid transparent;
            position: relative;
            overflow: hidden;
        }

        .article-card:hover {
            box-shadow: 0 8px 30px rgba(0,0,0,0.12);
            transform: translateY(-2px);
            border-left-color: #667eea;
        }

        .article-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 3px;
            background: linear-gradient(90deg, #667eea, #764ba2);
            opacity: 0;
            transition: opacity 0.3s ease;
        }

        .article-card:hover::before {
            opacity: 1;
        }

        .article-header {
            margin-bottom: 20px;
        }

        .article-title {
            font-size: 22px;
            font-weight: 700;
            color: #2d3748;
            text-decoration: none;
            margin-bottom: 12px;
            display: block;
            line-height: 1.4;
            transition: color 0.3s ease;
        }

        .article-title:hover {
            color: #667eea;
        }

        .article-meta {
            color: #718096;
            font-size: 14px;
            margin-bottom: 15px;
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
        }

        .article-meta span {
            display: flex;
            align-items: center;
            gap: 5px;
        }

        .article-meta span::before {
            content: '•';
            color: #cbd5e0;
            margin-right: 5px;
        }

        .article-meta span:first-child::before {
            content: '';
            margin-right: 0;
        }

        .article-summary {
            color: #4a5568;
            line-height: 1.7;
            margin-bottom: 20px;
            font-size: 15px;
            background: #f8fafc;
            padding: 15px;
            border-radius: 8px;
            border-left: 3px solid #e2e8f0;
        }

        .article-actions {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
        }

        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 8px;
            background: #f7fafc;
            color: #4a5568;
            text-decoration: none;
            font-size: 14px;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.3s ease;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 6px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }

        .btn:hover {
            transform: translateY(-1px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.15);
        }

        .btn-edit {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
        }

        .btn-edit:hover {
            background: linear-gradient(135deg, #5a6fd8, #6a4190);
            color: white;
        }

        .btn-delete {
            background: linear-gradient(135deg, #fc8181, #f56565);
            color: white;
        }

        .btn-delete:hover {
            background: linear-gradient(135deg, #f56565, #e53e3e);
        }

        .empty-state {
            text-align: center;
            padding: 80px 20px;
            color: #718096;
        }

        .empty-state h3 {
            margin-bottom: 15px;
            color: #4a5568;
            font-size: 24px;
            font-weight: 600;
        }

        .empty-state p {
            font-size: 16px;
            margin-bottom: 25px;
        }

        .pagination {
            display: flex;
            justify-content: center;
            margin-top: 40px;
            gap: 8px;
        }

        .pagination a {
            padding: 10px 18px;
            border: 1px solid #e2e8f0;
            border-radius: 8px;
            text-decoration: none;
            color: #4a5568;
            font-weight: 500;
            transition: all 0.3s ease;
            min-width: 45px;
            text-align: center;
        }

        .pagination a.active {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            border-color: transparent;
            box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
        }

        .pagination a:hover:not(.active) {
            background: #f7fafc;
            border-color: #cbd5e0;
            transform: translateY(-1px);
        }

        .message {
            padding: 16px 20px;
            border-radius: 12px;
            margin-bottom: 25px;
            font-weight: 500;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        .message.success {
            background: linear-gradient(135deg, #48bb78, #38a169);
            color: white;
            border: none;
        }

        .message.error {
            background: linear-gradient(135deg, #f56565, #e53e3e);
            color: white;
            border: none;
        }

        /* 调试信息样式 */
        .debug-info {
            background: linear-gradient(135deg, #edf2f7, #e2e8f0);
            padding: 15px;
            margin-bottom: 25px;
            border-radius: 12px;
            font-size: 13px;
            color: #4a5568;
            border-left: 4px solid #667eea;
        }

        /* 下拉菜单样式 */
        .nav-links {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .dropdown {
            position: relative;
        }

        .dropdown-toggle {
            display: flex;
            align-items: center;
            gap: 5px;
            padding: 10px 15px;
            text-decoration: none;
            color: inherit;
            transition: all 0.3s ease;
            border-radius: 6px;
        }

        .dropdown-toggle::after {
            content: '▼';
            font-size: 10px;
            transition: transform 0.3s ease;
        }

        .dropdown-toggle:hover::after {
            transform: rotate(180deg);
        }

        .dropdown-menu {
            position: absolute;
            top: 100%;
            left: 0;
            background: white;
            min-width: 200px;
            border-radius: 12px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.15);
            opacity: 0;
            visibility: hidden;
            transform: translateY(-10px);
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            z-index: 1000;
            border: 1px solid #e2e8f0;
            overflow: hidden;
        }

        .dropdown:hover .dropdown-menu {
            opacity: 1;
            visibility: visible;
            transform: translateY(0);
        }

        .dropdown-item {
            display: block;
            padding: 12px 20px;
            text-decoration: none;
            color: #4a5568;
            transition: all 0.3s ease;
            border-bottom: 1px solid #f7fafc;
        }

        .dropdown-item:last-child {
            border-bottom: none;
        }

        .dropdown-item:hover {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            transform: translateX(5px);
        }

        .dropdown-item i {
            margin-right: 8px;
            width: 16px;
            text-align: center;
        }

        /* 用户菜单样式 */
        .user-menu .dropdown-toggle {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            padding: 8px 15px;
            border-radius: 20px;
        }

        .user-menu .dropdown-menu {
            right: 0;
            left: auto;
        }

        /* 文章操作下拉菜单 */
        .article-actions .dropdown {
            display: inline-block;
        }

        .article-actions .dropdown-toggle {
            background: #f7fafc;
            padding: 6px 12px;
            border-radius: 6px;
            font-size: 14px;
        }

        .article-actions .dropdown-menu {
            min-width: 150px;
        }

        /* 响应式设计 */
        @media (max-width: 768px) {
            .articles-container {
                padding: 15px;
            }

            .page-header {
                padding: 20px;
                margin-bottom: 30px;
            }

            .page-title {
                font-size: 28px;
            }

            .article-card {
                padding: 20px;
            }

            .article-title {
                font-size: 20px;
            }

            .article-meta {
                flex-direction: column;
                gap: 8px;
            }

            .article-actions {
                flex-direction: column;
            }

            .btn {
                width: 100%;
                justify-content: center;
            }

            .pagination {
                flex-wrap: wrap;
            }

            .dropdown-menu {
                position: static;
                opacity: 1;
                visibility: visible;
                transform: none;
                box-shadow: none;
                margin-top: 10px;
            }

            .nav-links {
                flex-direction: column;
                gap: 5px;
            }
        }

        /* 加载动画 */
        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .article-card {
            animation: fadeInUp 0.5s ease-out;
        }

        /* 图标样式 */
        .btn::before {
            font-size: 14px;
        }

        .btn-edit::before {
            content: '✏️';
        }

        .btn-delete::before {
            content: '🗑️';
        }
    </style>
</head>
<body>
<!-- 导航栏 -->
<header>
    <nav>
        <div class="nav-container">
            <a href="${pageContext.request.contextPath}/index.jsp" class="logo">回声网络</a>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/article/list">首页</a>


                <!-- 用户相关下拉菜单 -->
                <c:if test="${not empty sessionScope.currentUser}">
                    <div class="dropdown user-menu">
                        <a href="javascript:void(0)" class="dropdown-toggle">
                            👤 ${sessionScope.currentUser.username}
                        </a>
                        <div class="dropdown-menu">
                            <a href="${pageContext.request.contextPath}/user/profile" class="dropdown-item">
                                👤 个人资料
                            </a>
                            <a href="${pageContext.request.contextPath}/user/settings" class="dropdown-item">
                                ⚙️ 账户设置
                            </a>
                            <a href="${pageContext.request.contextPath}/user/notifications" class="dropdown-item">
                                🔔 消息通知
                            </a>
                            <div class="dropdown-divider"></div>
                            <a href="${pageContext.request.contextPath}/user/logout" class="dropdown-item">
                                🚪 退出登录
                            </a>
                        </div>
                    </div>
                </c:if>

                <c:if test="${empty sessionScope.currentUser}">
                    <a href="${pageContext.request.contextPath}/user/login">登录</a>
                </c:if>
            </div>
        </div>
    </nav>
</header>

<div class="articles-container">
    <div class="page-header">
        <h1 class="page-title">我的文章</h1>
        <p>管理您发布的所有文章</p>
    </div>

    <!-- 消息显示 -->
    <c:if test="${not empty message}">
        <div class="message success">${message}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="message error">${error}</div>
    </c:if>

    <!-- 调试信息（正式环境可以移除） -->
    <div class="debug-info">
        调试信息:
        文章总数 = ${totalCount},
        当前页 = ${currentPage},
        总页数 = ${totalPages},
        当前用户ID = ${sessionScope.currentUser.id}
    </div>

    <c:choose>
        <c:when test="${not empty articles}">
            <c:forEach items="${articles}" var="article">
                <div class="article-card">
                    <div class="article-content">
                        <div class="article-header">
                            <a href="${pageContext.request.contextPath}/article/detail?id=${article.id}" class="article-title">
                                    ${article.title}
                            </a>
                            <div class="article-meta">
                                <span>发布时间: <fmt:formatDate value="${article.createTime}" pattern="yyyy-MM-dd HH:mm"/></span>
                                <span>阅读量: ${article.viewCount}</span>
                                <span>点赞: ${article.likeCount}</span>
                                <span>评论: ${article.commentCount}</span>
                            </div>
                        </div>

                        <c:if test="${not empty article.summary}">
                            <div class="article-summary">
                                    ${article.summary}
                            </div>
                        </c:if>

                        <div class="article-actions">
                            <a href="${pageContext.request.contextPath}/article/detail?id=${article.id}" class="btn">👁️ 查看</a>
                            <a href="${pageContext.request.contextPath}/article/edit?id=${article.id}" class="btn btn-edit">✏️ 编辑</a>

                            <!-- 更多操作下拉菜单 -->
                            <div class="dropdown">
                                <a href="javascript:void(0)" class="btn dropdown-toggle">⚙️ 更多</a>
                                <div class="dropdown-menu">
                                    <a href="${pageContext.request.contextPath}/wait.jsp" class="dropdown-item">
                                        🔗 分享文章
                                    </a>
                                    <a href="${pageContext.request.contextPath}/wait.jsp" class="dropdown-item">
                                        📊 数据统计
                                    </a>
                                    <a href="${pageContext.request.contextPath}/wait.jsp" class="dropdown-item">
                                        📋 复制文章
                                    </a>
                                    <div class="dropdown-divider"></div>
                                    <form action="${pageContext.request.contextPath}/article/delete" method="POST" style="display: block; margin: 0;">
                                        <input type="hidden" name="id" value="${article.id}">
                                        <button type="submit" class="dropdown-item" onclick="return confirm('确定要删除这篇文章吗？此操作不可恢复。')" style="background: none; border: none; width: 100%; text-align: left; color: inherit;">
                                            🗑️ 删除文章
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>

            <!-- 分页 -->
            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="${pageContext.request.contextPath}/article/my-articles?page=${currentPage - 1}">上一页</a>
                    </c:if>

                    <c:forEach begin="1" end="${totalPages}" var="p">
                        <c:choose>
                            <c:when test="${p == currentPage}">
                                <a href="#" class="active">${p}</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/article/my-articles?page=${p}">${p}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <a href="${pageContext.request.contextPath}/article/my-articles?page=${currentPage + 1}">下一页</a>
                    </c:if>
                </div>
            </c:if>
        </c:when>

        <c:otherwise>
            <div class="empty-state">
                <h3>您还没有发布过文章</h3>
                <p>开始您的创作之旅，分享您的想法和知识</p>
                <a href="${pageContext.request.contextPath}/article/publish" class="btn btn-edit" style="margin-top: 15px;">开始写文章</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script>
    // 删除文章确认
    document.addEventListener('DOMContentLoaded', function() {
        const deleteLinks = document.querySelectorAll('.btn-delete, .dropdown-item button');
        deleteLinks.forEach(link => {
            link.addEventListener('click', function(e) {
                if (!confirm('确定要删除这篇文章吗？此操作不可恢复。')) {
                    e.preventDefault();
                }
            });
        });

        // 移动端下拉菜单处理
        const dropdowns = document.querySelectorAll('.dropdown');
        dropdowns.forEach(dropdown => {
            const toggle = dropdown.querySelector('.dropdown-toggle');
            const menu = dropdown.querySelector('.dropdown-menu');

            toggle.addEventListener('click', function(e) {
                if (window.innerWidth <= 768) {
                    e.preventDefault();
                    const isOpen = menu.style.display === 'block';
                    menu.style.display = isOpen ? 'none' : 'block';
                }
            });
        });

        // 点击其他地方关闭下拉菜单
        document.addEventListener('click', function(e) {
            if (!e.target.closest('.dropdown')) {
                document.querySelectorAll('.dropdown-menu').forEach(menu => {
                    if (window.innerWidth <= 768) {
                        menu.style.display = 'none';
                    }
                });
            }
        });
    });
</script>
</body>
</html>