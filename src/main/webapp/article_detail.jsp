<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${article.title} - 回声网络</title>
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

        .container {
            max-width: 1000px;
            margin: 0 auto;
            padding: 20px;
        }

        /* 消息提示 */
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

        /* 文章内容区域 */
        .article-container {
            background: white;
            border-radius: 15px;
            padding: 40px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            margin-bottom: 30px;
        }

        .article-header {
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 1px solid #eaeaea;
        }

        .article-title {
            font-size: 2.5em;
            margin-bottom: 20px;
            color: #333;
            font-weight: 700;
            line-height: 1.3;
        }

        .article-meta {
            display: flex;
            gap: 20px;
            margin-bottom: 15px;
            color: #666;
            font-size: 0.95em;
        }

        .article-meta span {
            display: flex;
            align-items: center;
            gap: 5px;
        }

        /* 分类区域样式 */
        .article-categories-section {
            margin-bottom: 20px;
        }

        .categories-label {
            font-weight: 500;
            color: #666;
            margin-bottom: 8px;
            font-size: 0.95em;
        }

        .article-categories {
            display: flex;
            flex-wrap: wrap;
            gap: 8px;
        }

        .article-category {
            padding: 6px 15px;
            background: #f8f9fa;
            border-radius: 20px;
            font-size: 0.85em;
            color: #666;
            text-decoration: none;
            border: 1px solid #e1e5ee;
            transition: all 0.3s ease;
        }

        .article-category:hover {
            background: #6a11cb;
            color: white;
            border-color: #6a11cb;
        }

        .no-categories {
            color: #999;
            font-style: italic;
            font-size: 0.9em;
        }

        .article-stats {
            display: flex;
            gap: 25px;
            margin-top: 20px;
        }

        .stat-item {
            display: flex;
            align-items: center;
            gap: 8px;
            color: #666;
            font-size: 0.95em;
            padding: 8px 15px;
            background: #f8f9fa;
            border-radius: 20px;
            border: 1px solid #e1e5ee;
        }

        .like-btn {
            cursor: pointer;
            transition: all 0.3s ease;
            border: 1px solid #e1e5ee;
            background: #f8f9fa;
            border-radius: 20px;
            padding: 8px 15px;
            display: flex;
            align-items: center;
            gap: 8px;
            color: #666;
            font-size: 0.95em;
        }

        .like-btn:hover {
            background: #fff5f5;
            border-color: #ff6b6b;
            color: #ff6b6b;
        }

        .like-btn.liked {
            background: #fff5f5;
            border-color: #ff6b6b;
            color: #ff6b6b;
        }

        .article-content {
            font-size: 1.1em;
            line-height: 1.8;
            color: #444;
        }

        .article-content h1,
        .article-content h2,
        .article-content h3 {
            margin: 30px 0 15px 0;
            color: #333;
        }

        .article-content p {
            margin-bottom: 20px;
        }

        .article-content code {
            background: #f4f4f4;
            padding: 2px 6px;
            border-radius: 4px;
            font-family: 'Courier New', monospace;
        }

        .article-content pre {
            background: #2d2d2d;
            color: #f8f8f2;
            padding: 20px;
            border-radius: 8px;
            overflow-x: auto;
            margin: 20px 0;
        }

        .article-content blockquote {
            border-left: 4px solid #6a11cb;
            padding-left: 20px;
            margin: 20px 0;
            color: #666;
            font-style: italic;
        }

        /* 评论区域 */
        .comments-container {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
        }

        .comments-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
            padding-bottom: 15px;
            border-bottom: 1px solid #eaeaea;
        }

        .comments-title {
            font-size: 1.5em;
            color: #333;
            font-weight: 600;
        }

        .comment-count {
            color: #666;
            font-size: 0.95em;
        }

        /* 评论表单 */
        .comment-form {
            margin-bottom: 30px;
            padding: 25px;
            background: #f8f9fa;
            border-radius: 10px;
            border: 1px solid #e1e5ee;
        }

        .comment-form textarea {
            width: 100%;
            min-height: 120px;
            padding: 15px;
            border: 2px solid #e1e5ee;
            border-radius: 8px;
            font-size: 1em;
            resize: vertical;
            transition: all 0.3s ease;
            background: white;
            outline: none;
        }

        .comment-form textarea:focus {
            border-color: #6a11cb;
            box-shadow: 0 0 0 3px rgba(106, 17, 203, 0.1);
        }

        .comment-form .btn {
            margin-top: 15px;
            padding: 12px 25px;
            background: #6a11cb;
            color: white;
            border: none;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .comment-form .btn:hover {
            background: #2575fc;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(37, 117, 252, 0.3);
        }

        .login-prompt {
            text-align: center;
            padding: 30px;
            background: #f8f9fa;
            border-radius: 10px;
            border: 1px solid #e1e5ee;
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

        /* 评论列表 */
        .comment-list {
            margin-bottom: 30px;
        }

        .comment-item {
            padding: 20px 0;
            border-bottom: 1px solid #f0f0f0;
        }

        .comment-item:last-child {
            border-bottom: none;
        }

        .comment-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 10px;
        }

        .comment-author {
            font-weight: 600;
            color: #333;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .comment-author-avatar {
            width: 32px;
            height: 32px;
            border-radius: 50%;
            object-fit: cover;
        }

        .comment-time {
            color: #999;
            font-size: 0.85em;
        }

        .comment-content {
            color: #444;
            line-height: 1.6;
            padding-left: 40px;
        }

        /* 分页 */
        .pagination {
            display: flex;
            justify-content: center;
            gap: 8px;
            margin-top: 30px;
        }

        .page-btn {
            padding: 10px 15px;
            background: white;
            border: 1px solid #e1e5ee;
            border-radius: 8px;
            text-decoration: none;
            color: #666;
            transition: all 0.3s ease;
            font-size: 0.9em;
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
            color: #666;
        }

        .empty-state h3 {
            font-size: 1.3em;
            margin-bottom: 10px;
            color: #888;
        }

        /* 响应式设计 */
        @media (max-width: 768px) {
            .container {
                padding: 10px;
            }

            .article-container {
                padding: 25px;
            }

            .article-title {
                font-size: 2em;
            }

            .article-meta {
                flex-direction: column;
                gap: 8px;
            }

            .article-stats {
                flex-wrap: wrap;
                gap: 10px;
            }

            .comments-container {
                padding: 20px;
            }

            .comments-header {
                flex-direction: column;
                align-items: flex-start;
                gap: 10px;
            }

            .comment-header {
                flex-direction: column;
                align-items: flex-start;
                gap: 5px;
            }

            .comment-content {
                padding-left: 0;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <!-- 消息提示 -->
    <c:if test="${not empty message}">
        <div class="message success">${message}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="message error">${error}</div>
    </c:if>

    <!-- 文章内容 -->
    <div class="article-container">
        <div class="article-header">
            <h1 class="article-title">${article.title}</h1>

            <div class="article-meta">
                <span>作者: ${article.authorName}</span>
                <span>发布于: <fmt:formatDate value="${article.createTime}" pattern="yyyy-MM-dd HH:mm"/></span>
                <span>阅读: ${article.viewCount}</span>
            </div>

            <!-- 分类区域 -->
            <div class="article-categories-section">
                <div class="categories-label">分类:</div>
                <div class="article-categories">
                    <c:choose>
                        <c:when test="${not empty article.categories && fn:length(article.categories) > 0}">
                            <c:forEach var="category" items="${article.categories}">
                                <a href="${pageContext.request.contextPath}/article/list?categoryId=${category.id}"
                                   class="article-category">${category.name}</a>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <span class="no-categories">无</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="article-stats">
                <button class="like-btn stat-item ${hasLiked ? 'liked' : ''}"
                        id="likeBtn"
                        data-article-id="${article.id}">
                    <span>❤️</span>
                    <span id="likeCount">${article.likeCount}</span> 点赞
                </button>
                <div class="stat-item">
                    <span>💬</span>
                    <span>${totalComments}</span> 评论
                </div>
                <div class="stat-item">
                    <span>👁️</span>
                    <span>${article.viewCount}</span> 阅读
                </div>
            </div>
        </div>

        <div class="article-content">
            ${article.htmlContent}
        </div>
    </div>

    <!-- 评论区域 -->
    <div class="comments-container">
        <div class="comments-header">
            <h2 class="comments-title">评论</h2>
            <span class="comment-count">共 ${totalComments} 条评论</span>
        </div>

        <!-- 评论表单 -->
        <c:choose>
            <c:when test="${not empty sessionScope.currentUser}">
                <form class="comment-form" action="${pageContext.request.contextPath}/article/comment" method="POST">
                    <input type="hidden" name="articleId" value="${article.id}">
                    <textarea name="content" placeholder="写下你的评论..." required maxlength="1000"></textarea>
                    <button type="submit" class="btn">发表评论</button>
                </form>
            </c:when>
            <c:otherwise>
                <div class="login-prompt">
                    <p>请<a href="${pageContext.request.contextPath}/user/login">登录</a>后发表评论</p>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- 评论列表 -->
        <div class="comment-list">
            <c:choose>
                <c:when test="${not empty comments}">
                    <c:forEach var="comment" items="${comments}">
                        <div class="comment-item">
                            <div class="comment-header">
                                <div class="comment-author">
                                    <img src="${pageContext.request.contextPath}${not empty comment.authorAvatar ? comment.authorAvatar : '/images/avatar-default.png'}"
                                         alt="头像" class="comment-author-avatar">
                                        ${comment.authorName}
                                </div>
                                <span class="comment-time">
                                    <fmt:formatDate value="${comment.createTime}" pattern="yyyy-MM-dd HH:mm"/>
                                </span>
                            </div>
                            <div class="comment-content">${comment.content}</div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <h3>暂无评论</h3>
                        <p>快来发表第一条评论吧！</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- 评论分页 -->
        <c:if test="${totalCommentPages > 1}">
            <div class="pagination">
                <c:if test="${currentCommentPage > 1}">
                    <a href="${pageContext.request.contextPath}/article/detail?id=${article.id}&commentPage=${currentCommentPage-1}"
                       class="page-btn">上一页</a>
                </c:if>

                <c:forEach begin="1" end="${totalCommentPages}" var="pageNum">
                    <a href="${pageContext.request.contextPath}/article/detail?id=${article.id}&commentPage=${pageNum}"
                       class="page-btn ${pageNum == currentCommentPage ? 'active' : ''}">${pageNum}</a>
                </c:forEach>

                <c:if test="${currentCommentPage < totalCommentPages}">
                    <a href="${pageContext.request.contextPath}/article/detail?id=${article.id}&commentPage=${currentCommentPage+1}"
                       class="page-btn">下一页</a>
                </c:if>
            </div>
        </c:if>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const likeBtn = document.getElementById('likeBtn');
        const likeCount = document.getElementById('likeCount');

        // 点赞功能
        if (likeBtn) {
            likeBtn.addEventListener('click', function() {
                const articleId = this.getAttribute('data-article-id');
                const isLiked = this.classList.contains('liked');

                // 发送点赞请求
                fetch('${pageContext.request.contextPath}/article/' + (isLiked ? 'unlike' : 'like'), {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: 'id=' + articleId
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            // 更新UI
                            if (isLiked) {
                                this.classList.remove('liked');
                                likeCount.textContent = parseInt(likeCount.textContent) - 1;
                            } else {
                                this.classList.add('liked');
                                likeCount.textContent = parseInt(likeCount.textContent) + 1;
                            }
                        } else {
                            alert('操作失败: ' + data.message);
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert('网络错误，请稍后重试');
                    });
            });
        }

        // 评论表单验证
        const commentForm = document.querySelector('.comment-form');
        if (commentForm) {
            commentForm.addEventListener('submit', function(e) {
                const textarea = this.querySelector('textarea');
                if (textarea.value.trim().length === 0) {
                    e.preventDefault();
                    alert('评论内容不能为空');
                    textarea.focus();
                }
            });
        }

        // 自动调整评论框高度
        const commentTextarea = document.querySelector('textarea[name="content"]');
        if (commentTextarea) {
            commentTextarea.addEventListener('input', function() {
                this.style.height = 'auto';
                this.style.height = (this.scrollHeight) + 'px';
            });
        }
    });
</script>
</body>
</html>