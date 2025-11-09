<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>文章列表 - 回声网络</title>
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

        .nav-actions {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        /* 分类筛选按钮 */
        .filter-btn {
            padding: 10px 20px;
            background: #6a11cb;
            color: white;
            border: none;
            border-radius: 8px;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .filter-btn:hover {
            background: #2575fc;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(37, 117, 252, 0.3);
        }

        .container {
            max-width: 1200px;
            margin: 90px auto 40px;
            padding: 0 20px;
        }

        .page-header {
            text-align: center;
            margin-bottom: 40px;
        }

        .page-header h1 {
            font-size: 2.5em;
            margin-bottom: 10px;
            color: #333;
            font-weight: 700;
        }

        .page-header p {
            color: #666;
            font-size: 1.1em;
        }

        /* 当前筛选状态 */
        .filter-status {
            text-align: center;
            margin-bottom: 30px;
            padding: 15px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 3px 10px rgba(0,0,0,0.05);
        }

        .current-filter {
            font-weight: 500;
            color: #6a11cb;
        }

        .clear-filter {
            color: #666;
            text-decoration: none;
            margin-left: 15px;
            font-size: 0.9em;
        }

        .clear-filter:hover {
            color: #6a11cb;
        }

        /* 用户状态提示 */
        .user-status {
            text-align: center;
            margin-bottom: 30px;
            padding: 20px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 3px 10px rgba(0,0,0,0.05);
        }

        .login-prompt {
            color: #666;
        }

        .login-prompt a {
            color: #6a11cb;
            text-decoration: none;
            font-weight: 500;
        }

        .login-prompt a:hover {
            text-decoration: underline;
        }

        /* 文章列表 */
        .articles-container {
            display: grid;
            gap: 25px;
            margin-bottom: 40px;
        }

        .article-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            transition: all 0.3s ease;
            border: 1px solid #f0f0f0;
        }

        .article-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0,0,0,0.12);
        }

        .article-header {
            margin-bottom: 15px;
        }

        .article-title {
            font-size: 1.4em;
            margin-bottom: 10px;
            color: #333;
            text-decoration: none;
            font-weight: 600;
            line-height: 1.4;
            display: block;
        }

        .article-title:hover {
            color: #6a11cb;
        }

        .article-title.disabled {
            color: #666;
            cursor: not-allowed;
        }

        .article-title.disabled:hover {
            color: #666;
        }

        .article-author {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-bottom: 15px;
        }

        .author-avatar {
            width: 32px;
            height: 32px;
            border-radius: 50%;
            object-fit: cover;
        }

        .author-name {
            color: #666;
            font-size: 0.95em;
        }

        .article-categories {
            display: flex;
            flex-wrap: wrap;
            gap: 8px;
            margin-bottom: 15px;
        }

        .article-category {
            padding: 4px 12px;
            background: #f8f9fa;
            border-radius: 15px;
            font-size: 0.8em;
            color: #666;
            text-decoration: none;
        }

        .article-category:hover {
            background: #e9ecef;
        }

        .article-summary {
            color: #666;
            line-height: 1.6;
            margin-bottom: 20px;
        }

        .article-stats {
            display: flex;
            gap: 20px;
            color: #999;
            font-size: 0.9em;
        }

        .stat-item {
            display: flex;
            align-items: center;
            gap: 5px;
        }

        /* 模态框样式 */
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            z-index: 1000;
            align-items: center;
            justify-content: center;
        }

        .modal.active {
            display: flex;
        }

        .modal-content {
            background: white;
            border-radius: 15px;
            width: 90%;
            max-width: 600px;
            max-height: 80vh;
            overflow: hidden;
            box-shadow: 0 20px 40px rgba(0,0,0,0.2);
        }

        .modal-header {
            padding: 20px 25px;
            border-bottom: 1px solid #eaeaea;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .modal-header h3 {
            font-size: 1.5em;
            color: #333;
            font-weight: 600;
        }

        .close-modal {
            background: none;
            border: none;
            font-size: 1.5em;
            cursor: pointer;
            color: #666;
        }

        .close-modal:hover {
            color: #333;
        }

        .modal-body {
            padding: 25px;
            max-height: 400px;
            overflow-y: auto;
        }

        .modal-footer {
            padding: 20px 25px;
            border-top: 1px solid #eaeaea;
            display: flex;
            justify-content: flex-end;
            gap: 15px;
        }

        /* 分类选择区域 */
        .categories-container {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            margin-bottom: 20px;
        }

        .category-checkbox {
            display: none;
        }

        .category-label {
            display: inline-block;
            padding: 10px 20px;
            background: #f8f9fa;
            border: 2px solid #e1e5ee;
            border-radius: 25px;
            cursor: pointer;
            transition: all 0.3s ease;
            font-weight: 500;
            color: #666;
            user-select: none;
        }

        .category-checkbox:checked + .category-label {
            background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
            color: white;
            border-color: #6a11cb;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(106, 17, 203, 0.3);
        }

        .category-label:hover {
            border-color: #6a11cb;
            transform: translateY(-2px);
        }/* 分页 */
        .pagination {
            display: flex;
            justify-content: center;
            gap: 10px;
            margin-top: 40px;
        }

        .page-btn {
            padding: 10px 15px;
            background: white;
            border: 1px solid #e1e5ee;
            border-radius: 8px;
            text-decoration: none;
            color: #666;
            transition: all 0.3s ease;
        }


        .search-form {
            margin-right: 15px;
        }

        .search-container {
            display: flex;
            align-items: center;
            background: white;
            border: 2px solid #e1e5ee;
            border-radius: 25px;
            padding: 5px;
            transition: all 0.3s ease;
        }

        .search-container:focus-within {
            border-color: #6a11cb;
            box-shadow: 0 0 0 3px rgba(106, 17, 203, 0.1);
        }

        .search-input {
            border: none;
            outline: none;
            padding: 8px 15px;
            border-radius: 20px;
            width: 200px;
            font-size: 0.9em;
        }

        .search-btn {
            background: #6a11cb;
            color: white;
            border: none;
            border-radius: 20px;
            padding: 8px 15px;
            cursor: pointer;
            font-size: 0.9em;
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .search-btn:hover {
            background: #2575fc;
        }

        /* 移动端搜索样式 */
        @media (max-width: 768px) {
            .search-form {
                margin-right: 10px;
            }

            .search-input {
                width: 150px;
                padding: 6px 12px;
            }

            .search-btn {
                padding: 6px 12px;
            }

            .page-btn:hover, .page-btn.active {
                background: #6a11cb;
                color: white;
                border-color: #6a11cb;
            }

            .page-btn.disabled {
                opacity: 0.5;
                cursor: not-allowed;
            }

            /* 空状态 */
            .empty-state {
                text-align: center;
                padding: 60px 20px;
                background: white;
                border-radius: 15px;
                box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
            }

            .empty-state h3 {
                font-size: 1.5em;
                margin-bottom: 15px;
                color: #666;
            }

            .empty-state p {
                color: #888;
                margin-bottom: 25px;
            }

            .btn {
                display: inline-block;
                padding: 12px 25px;
                background: #6a11cb;
                color: white;
                text-decoration: none;
                border-radius: 8px;
                font-weight: 600;
                transition: all 0.3s ease;
            }

            .btn:hover {
                background: #2575fc;
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(37, 117, 252, 0.3);
            }

            @media (max-width: 768px) {
                .container {
                    margin-top: 70px;
                }

                .page-header h1 {
                    font-size: 2em;
                }

                .nav-actions {
                    gap: 10px;
                }

                .filter-btn {
                    padding: 8px 15px;
                    font-size: 0.9em;
                }

                .categories-container {
                    justify-content: center;
                }
            }
        }
    </style>
</head>
<body>
<!-- 页面头部 -->
<header class="header">
    <nav class="nav">
        <a href="${pageContext.request.contextPath}/index.jsp" class="logo">回声网络</a >
        <div class="nav-actions">
            <form action="${pageContext.request.contextPath}/article/list" method="get" class="search-form" id="searchForm">
                <div class="search-container">
                    <input type="text" name="keyword" value="${param.keyword}" placeholder="搜索文章..." class="search-input">
                    <button type="submit" class="search-btn">搜索</button>
                </div>
            </form>

            <button class="filter-btn" id="openCategoryModal">
                <span>分类筛选</span>
            </button>
        </div>
    </nav>
</header><div class="container">
    <div class="page-header">
        <h1>发现精彩内容</h1>
        <p>阅读来自回声网络创作者的优质文章</p >
    </div>

    <!-- 当前筛选状态 -->
    <c:if test="${not empty currentCategory}">
        <div class="filter-status">
            <span>当前筛选: <span class="current-filter">${currentCategory.name}</span></span>
            <a href="#" class="clear-filter">清除筛选</a >
        </div>
    </c:if>

    <!-- 用户登录状态提示 -->
    <div class="user-status">
        <c:choose>
            <c:when test="${not empty sessionScope.currentUser}">
                <p>欢迎回来，<strong>${sessionScope.currentUser.username}</strong>！
                    <a href="${pageContext.request.contextPath}/article/publish" style="color: #6a11cb; text-decoration: none; font-weight: 500;">开始写作</a ></p >
            </c:when>
            <c:otherwise>
                <p class="login-prompt">
                    登录后可以查看文章详情、点赞和评论。
                    <a href="${pageContext.request.contextPath}/user/login">立即登录</a > 或
                    <a href="${pageContext.request.contextPath}/user/register">注册账号</a >
                </p >
            </c:otherwise>
        </c:choose>



    </div>
    <!-- 文章列表 -->
    <div class="articles-container">
        <c:choose>
            <c:when test="${not empty articles}">
                <c:forEach items="${articles}" var="article">
                    <div class="article-card">
                        <!-- 移除了多余的 article-item div -->
                        <div class="article-header">
                            <a href="${pageContext.request.contextPath}/article/detail?id=${article.id}"
                               class="article-title">${article.title}</a>
                            <div class="article-author">
                                <!-- 如果有作者头像可以在这里显示 -->
                                <span class="author-name">作者: ${article.authorName}</span>
                            </div>
                        </div>

                        <!-- 分类 -->
                        <c:if test="${not empty article.categories}">
                            <div class="article-categories">
                                <c:forEach var="category" items="${article.categories}">
                                    <a href="${pageContext.request.contextPath}/article/by-category?id=${category.id}"
                                       class="article-category">${category.name}</a>
                                </c:forEach>
                            </div>
                        </c:if>

                        <p class="article-summary">${article.summary}</p>

                        <div class="article-stats">
                            <div class="stat-item">👁️ ${article.viewCount} 阅读</div>
                            <div class="stat-item">💬 ${article.commentCount} 评论</div>
                            <div class="stat-item">❤️ ${article.likeCount} 点赞</div>
                            <div class="stat-item">📅
                                <fmt:formatDate value="${article.publishTime}" pattern="yyyy-MM-dd"/>
                            </div>
                        </div>
                    </div>
                    <!-- 注意：这里只有一个闭合div，对应article-card -->
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <h3>暂无文章</h3>
                    <p>还没有人分享文章，快来发布第一篇文章吧！</p>
                    <c:choose>
                        <c:when test="${not empty sessionScope.currentUser}">
                            <a href="${pageContext.request.contextPath}/article/publish" class="btn">发布第一篇文章</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/user/register" class="btn">注册并发布文章</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

<!-- 分页控件 -->
<div class="pagination">
    <!-- 上一页 -->
    <c:if test="${currentPage > 1}">
        <a href="${pageContext.request.contextPath}/article/list、page=${currentPage-1}
"<c:if test="${not empty currentCategoryId}">&categoryId=${currentCategoryId}</c:if>
    <c:if test="${not empty param.keyword}">&keyword=${param.keyword}</c:if>"
    class="page-btn">上一页</a >
    </c:if>

    <!-- 页码 -->
    <c:forEach begin="1" end="${totalPages}" var="pageNum">
        <c:choose>
            <c:when test="${pageNum == currentPage}">
                <span class="page-btn active">${pageNum}</span>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/article/list?page=${pageNum}
                   <c:if test="${not empty currentCategoryId}">&categoryId=${currentCategoryId}</c:if>
                   <c:if test="${not empty param.keyword}">&keyword=${param.keyword}</c:if>"
                   class="page-btn">${pageNum}</a >
            </c:otherwise>
        </c:choose>
    </c:forEach>

    <!-- 下一页 -->
    <c:if test="${currentPage < totalPages}">
        <a href="${pageContext.request.contextPath}/article/list?page=${currentPage + 1}
           <c:if test="${not empty currentCategoryId}">&categoryId=${currentCategoryId}</c:if>
           <c:if test="${not empty param.keyword}">&keyword=${param.keyword}</c:if>"
           class="page-btn">下一页</a >
    </c:if>
</div>
<!-- 分类筛选模态框 -->
<div class="modal" id="categoryModal">
    <div class="modal-content">
        <div class="modal-header">
            <h3>选择文章分类</h3>
            <button class="close-modal" id="closeCategoryModal">&times;</button>
        </div>
        <div class="modal-body">
            <div class="categories-container" id="modalCategories">
                <!-- 分类将通过JavaScript动态加载 -->
                <c:forEach var="category" items="${categories}">
                    <a href="${pageContext.request.contextPath}/article/by-category?=${category.id}"
                       class="category-label ${currentCategoryId == category.id ? 'active' : ''}">
                        ${category.name}
                    </a >
                </c:forEach>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-secondary" id="cancelCategorySelection">取消</button>
            <a href="${pageContext.request.contextPath}/article/list" class="btn btn-primary">查看全部</a >
        </div>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // 分类筛选模态框相关元素
        const openCategoryModalBtn = document.getElementById('openCategoryModal');
        const closeCategoryModalBtn = document.getElementById('closeCategoryModal');
        const cancelCategorySelectionBtn = document.getElementById('cancelCategorySelection');
        const categoryModal = document.getElementById('categoryModal');

        // 打开模态框
        openCategoryModalBtn.addEventListener('click', function() {
            categoryModal.classList.add('active');
        });

        // 关闭模态框
        function closeModal() {
            categoryModal.classList.remove('active');
        }

        closeCategoryModalBtn.addEventListener('click', closeModal);
        cancelCategorySelectionBtn.addEventListener('click', closeModal);

        // 点击模态框外部关闭
        categoryModal.addEventListener('click', function(e) {
            if (e.target === categoryModal) {
                closeModal();
            }
        });

        // 添加平滑滚动效果
        document.querySelectorAll('a[href^="#"]:not(.category-label)').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                const href = this.getAttribute('href');
                if (href !== '#') { // 只处理真正的锚点，不是空链接
                    e.preventDefault();
                    const target = document.querySelector(href);
                    if (target) {
                        target.scrollIntoView({
                            behavior: 'smooth'
                        });
                    }
                }
            });
        });

        // 添加：点击分类链接后自动关闭模态框
        document.querySelectorAll('.category-label').forEach(link => {
            link.addEventListener('click', function() {
                closeModal();
            });
        });
    });
    // 显示登录提示
    function showLoginPrompt() {
        alert('请先登录后再查看文章详情');
         //可选：跳转到登录页面
        // window.location.href = '${pageContext.request.contextPath}/';
    }

    // 处理键盘事件
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            document.querySelectorAll('.modal.active').forEach(modal => {
                modal.classList.remove('active');
            });
        }
    });
</script>
</body>
</html>