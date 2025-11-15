<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        <c:choose>
            <c:when test="${action == 'edit'}">编辑文章</c:when>
            <c:otherwise>发布新文章</c:otherwise>
        </c:choose>
        - 回声网络
    </title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background-color: #f8f9fa;
            color: #333;
            line-height: 1.6;
            padding: 20px;
        }

        .container {
            max-width: 900px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            padding: 30px;
        }

        .header {
            text-align: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 1px solid #eaeaea;
        }

        .header h1 {
            color: #2c3e50;
            margin-bottom: 10px;
            font-weight: 600;
        }

        .header p {
            color: #7f8c8d;
            font-size: 16px;
        }

        .form-section {
            margin-bottom: 25px;
            padding: 20px;
            border: 1px solid #eaeaea;
            border-radius: 8px;
        }

        .form-section-title {
            font-size: 18px;
            color: #2c3e50;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 1px solid #f0f0f0;
            font-weight: 600;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 500;
            color: #444;
        }

        .form-control {
            width: 100%;
            padding: 12px 15px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 15px;
            transition: border-color 0.3s;
        }

        .form-control:focus {
            border-color: #6a11cb;
            outline: none;
            box-shadow: 0 0 0 2px rgba(106, 17, 203, 0.1);
        }

        textarea.form-control {
            min-height: 120px;
            resize: vertical;
        }

        .categories-section {
            margin-top: 15px;
        }

        .selected-categories {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            margin: 10px 0;
            min-height: 50px;
            padding: 12px;
            border: 1px dashed #ddd;
            border-radius: 6px;
        }

        .category-tag {
            background: linear-gradient(135deg, #6a11cb, #2575fc);
            color: white;
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 14px;
            display: flex;
            align-items: center;
            gap: 6px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .remove-category {
            cursor: pointer;
            font-weight: bold;
            font-size: 16px;
            opacity: 0.8;
            transition: opacity 0.2s;
        }

        .remove-category:hover {
            opacity: 1;
        }

        .no-categories {
            color: #999;
            font-style: italic;
            align-self: center;
            width: 100%;
            text-align: center;
        }

        .category-manage-btn {
            background: linear-gradient(135deg, #6a11cb, #2575fc);
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 6px;
            cursor: pointer;
            font-size: 15px;
            font-weight: 500;
            transition: transform 0.2s, box-shadow 0.2s;
            box-shadow: 0 3px 8px rgba(106, 17, 203, 0.3);
        }

        .category-manage-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(106, 17, 203, 0.4);
        }

        .action-buttons {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 30px;
        }

        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 16px;
            font-weight: 500;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            justify-content: center;
        }

        .btn-primary {
            background: linear-gradient(135deg, #6a11cb, #2575fc);
            color: white;
            box-shadow: 0 4px 10px rgba(106, 17, 203, 0.3);
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 15px rgba(106, 17, 203, 0.4);
        }

        .btn-secondary {
            background: #95a5a6;
            color: white;
        }

        .btn-secondary:hover {
            background: #7f8c8d;
        }

        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            animation: fadeIn 0.3s;
        }

        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }

        .modal-content {
            background-color: #fff;
            margin: 5% auto;
            padding: 0;
            border-radius: 10px;
            width: 90%;
            max-width: 600px;
            max-height: 80vh;
            overflow: hidden;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
            animation: slideIn 0.3s;
        }

        @keyframes slideIn {
            from { transform: translateY(-50px); opacity: 0; }
            to { transform: translateY(0); opacity: 1; }
        }

        .modal-header {
            padding: 20px 25px;
            border-bottom: 1px solid #eee;
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: linear-gradient(135deg, #6a11cb, #2575fc);
            color: white;
        }

        .modal-header h3 {
            margin: 0;
            font-weight: 600;
        }

        .close-modal {
            background: none;
            border: none;
            font-size: 24px;
            cursor: pointer;
            color: white;
            opacity: 0.8;
            transition: opacity 0.2s;
        }

        .close-modal:hover {
            opacity: 1;
        }

        .modal-body {
            padding: 25px;
            max-height: 400px;
            overflow-y: auto;
        }

        .categories-container {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 15px;
            margin-bottom: 25px;
        }

        .category-item {
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 15px;
            transition: all 0.3s;
            cursor: pointer;
        }

        .category-item:hover {
            border-color: #6a11cb;
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }

        .category-item.selected {
            border-color: #6a11cb;
            background-color: rgba(106, 17, 203, 0.05);
        }

        .category-checkbox {
            display: flex;
            align-items: center;
            cursor: pointer;
        }

        .category-info {
            flex: 1;
        }

        .category-info strong {
            display: block;
            color: #2c3e50;
            margin-bottom: 5px;
        }

        .category-info span {
            font-size: 13px;
            color: #7f8c8d;
        }

        .word-count {
            text-align: right;
            font-size: 13px;
            color: #777;
            margin-top: 5px;
        }

        .message {
            padding: 12px 16px;
            border-radius: 6px;
            margin-bottom: 20px;
            font-weight: 500;
        }

        .message.success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .message.error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        @media (max-width: 768px) {
            .container {
                padding: 15px;
            }

            .form-section {
                padding: 15px;
            }

            .action-buttons {
                flex-direction: column;
            }

            .btn {
                width: 100%;
            }

            .categories-container {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>
            <c:choose>
                <c:when test="${action == 'edit'}">编辑文章</c:when>
                <c:otherwise>发布新文章</c:otherwise>
            </c:choose>
        </h1>
        <p>
            <c:choose>
                <c:when test="${action == 'edit'}">修改您的文章内容，让文章更加完美</c:when>
                <c:otherwise>选择适合您文章的分类，让更多读者发现您的内容</c:otherwise>
            </c:choose>
        </p>
    </div>

    <div class="message-container" id="messageContainer">
        <c:if test="${not empty message}">
            <div class="message success">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="message error">${error}</div>
        </c:if>
    </div>

    <!-- 文章表单 -->
    <form id="articleForm"
          action="${pageContext.request.contextPath}/article/${action == 'edit' ? 'edit' : 'publish'}"
          method="POST">

        <!-- 编辑模式下隐藏的文章ID -->
        <c:if test="${action == 'edit' && not empty article}">
            <input type="hidden" name="id" value="${article.id}">
        </c:if>

        <div class="form-section">
            <h3 class="form-section-title">基本信息</h3>

            <div class="form-group">
                <label for="title">文章标题 *</label>
                <input type="text" id="title" name="title" class="form-control"
                       placeholder="请输入文章标题" required
                       value="${not empty article ? article.title : ''}">
            </div>

            <div class="form-group">
                <label for="summary">文章摘要</label>
                <textarea id="summary" name="summary" class="form-control"
                          placeholder="请简要描述文章内容，这将在文章列表中显示">${not empty article ? article.summary : ''}</textarea>
                <div class="word-count" id="summaryCount">0/200</div>
            </div>
        </div>

        <div class="form-section">
            <h3 class="form-section-title">内容编辑</h3>

            <div class="form-group">
                <label for="content">文章内容 *</label>
                <textarea id="content" name="content" class="form-control"
                          placeholder="请输入文章内容，支持Markdown格式" required>${not empty article ? article.content : ''}</textarea>
                <div class="word-count" id="contentCount">0 字</div>
            </div>
        </div>

        <div class="form-section">
            <h3 class="form-section-title">分类选择</h3>
            <div class="categories-section">
                <label>文章分类（可多选）</label>
                <div class="selected-categories" id="selectedCategories">
                    <div class="no-categories">暂未选择分类</div>
                </div>
                <button type="button" class="category-manage-btn" id="openCategoryModal">
                    选择分类
                </button>

                <!-- 隐藏字段用于存储分类ID -->
                <div id="categoryIdsContainer">
                    <!-- 动态添加的隐藏输入框 -->
                </div>
            </div>
        </div>

        <div class="action-buttons">
            <button type="submit" class="btn btn-primary" id="submitBtn">
                <c:choose>
                    <c:when test="${action == 'edit'}">更新文章</c:when>
                    <c:otherwise>发布文章</c:otherwise>
                </c:choose>
            </button>
            <a href="${pageContext.request.contextPath}/article/my-articles" class="btn btn-secondary">取消</a>
        </div>
    </form>
</div>

<!-- 分类管理模态框 -->
<div class="modal" id="categoryModal">
    <div class="modal-content">
        <div class="modal-header">
            <h3>选择文章分类</h3>
            <button class="close-modal" id="closeCategoryModal">&times;</button>
        </div>
        <div class="modal-body">
            <div class="categories-container" id="modalCategories">
                <!-- 分类将通过JavaScript动态加载 -->
            </div>
        </div>
        <div class="modal-footer" style="padding: 15px 25px; border-top: 1px solid #eee; text-align: right;">
            <button type="button" class="btn btn-secondary" id="cancelCategorySelection">取消</button>
            <button type="button" class="btn btn-primary" id="confirmCategorySelection">确定</button>
        </div>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // 获取所有DOM元素
        var elements = {
            openCategoryModalBtn: document.getElementById('openCategoryModal'),
            closeCategoryModalBtn: document.getElementById('closeCategoryModal'),
            cancelCategorySelectionBtn: document.getElementById('cancelCategorySelection'),
            confirmCategorySelectionBtn: document.getElementById('confirmCategorySelection'),
            categoryModal: document.getElementById('categoryModal'),
            modalCategories: document.getElementById('modalCategories'),
            selectedCategories: document.getElementById('selectedCategories'),
            summaryInput: document.getElementById('summary'),
            contentInput: document.getElementById('content'),
            titleInput: document.getElementById('title'),
            summaryCount: document.getElementById('summaryCount'),
            contentCount: document.getElementById('contentCount'),
            submitBtn: document.getElementById('submitBtn'),
            messageContainer: document.getElementById('messageContainer'),
            articleForm: document.getElementById('articleForm')
        };

        // 已选分类数组
        var selectedCategoriesList = [];

        // 分类数据
        var categoriesData = [
            { id: 1, name: '技术教程', description: '编程、开发、技术相关文章' },
            { id: 2, name: '生活随笔', description: '日常生活、感悟分享' },
            { id: 3, name: '读书笔记', description: '学习笔记、知识总结' },
            { id: 4, name: '旅行游记', description: '旅行见闻、景点推荐' },
            { id: 5, name: '美食分享', description: '美食制作、餐厅推荐' },
            { id: 6, name: '数码产品', description: '数码产品评测与推荐' }
        ];

        // ==================== 初始化页面数据 ====================
        function initializePageData() {
            // 初始化字数统计
            if (elements.summaryInput && elements.summaryCount) {
                var summaryCount = elements.summaryInput.value.length;
                elements.summaryCount.textContent = summaryCount + '/200';
            }

            if (elements.contentInput && elements.contentCount) {
                var contentCount = elements.contentInput.value.length;
                elements.contentCount.textContent = contentCount + ' 字';
            }

            // 初始化分类选择
            updateSelectedCategoriesDisplay();
            updateHiddenCategoryFields();
        }

        // ==================== 模态框控制 ====================
        if (elements.openCategoryModalBtn) {
            elements.openCategoryModalBtn.addEventListener('click', function() {
                if (elements.categoryModal) {
                    elements.categoryModal.style.display = 'block';
                    loadCategories();
                }
            });
        }

        function closeModal() {
            if (elements.categoryModal) {
                elements.categoryModal.style.display = 'none';
            }
        }

        if (elements.closeCategoryModalBtn) {
            elements.closeCategoryModalBtn.addEventListener('click', closeModal);
        }

        if (elements.cancelCategorySelectionBtn) {
            elements.cancelCategorySelectionBtn.addEventListener('click', closeModal);
        }

        if (elements.categoryModal) {
            elements.categoryModal.addEventListener('click', function(e) {
                if (e.target === elements.categoryModal) {
                    closeModal();
                }
            });
        }

        // ==================== 分类管理功能 ====================
        function loadCategories() {
            if (!elements.modalCategories) return;
            displayCategories(categoriesData);
        }

        function displayCategories(categories) {
            elements.modalCategories.innerHTML = '';

            if (categories.length === 0) {
                elements.modalCategories.innerHTML = '<div style="text-align: center; color: #999; padding: 20px;">暂无分类</div>';
                return;
            }

            categories.forEach(function(category) {
                var categoryElement = document.createElement('div');
                var className = 'category-item';
                if (isCategorySelected(category.id)) {
                    className += ' selected';
                }
                categoryElement.className = className;

                var isSelected = isCategorySelected(category.id);
                categoryElement.innerHTML =
                    '<label class="category-checkbox">' +
                    '<input type="checkbox" value="' + category.id + '" ' +
                    (isSelected ? 'checked' : '') + '>' +
                    '<div class="category-info">' +
                    '<strong>' + category.name + '</strong>' +
                    '<span>' + (category.description || '暂无描述') + '</span>' +
                    '</div>' +
                    '</label>';

                // 添加点击事件
                categoryElement.addEventListener('click', function(e) {
                    if (e.target.type !== 'checkbox') {
                        var checkbox = this.querySelector('input[type="checkbox"]');
                        checkbox.checked = !checkbox.checked;
                        handleCategorySelection(category.id, category.name, checkbox.checked);

                        if (checkbox.checked) {
                            this.classList.add('selected');
                        } else {
                            this.classList.remove('selected');
                        }
                    }
                });


                // 复选框变化事件
                var checkbox = categoryElement.querySelector('input[type="checkbox"]');
                checkbox.addEventListener('change', function() {
                    handleCategorySelection(category.id, category.name, this.checked);

                    if (this.checked) {
                        categoryElement.classList.add('selected');
                    } else {
                        categoryElement.classList.remove('selected');
                    }
                });

                elements.modalCategories.appendChild(categoryElement);
            });
        }

        function isCategorySelected(categoryId) {
            return selectedCategoriesList.some(function(cat) {
                return cat.id === categoryId;
            });
        }

        function handleCategorySelection(categoryId, categoryName, isSelected) {
            if (isSelected) {
                if (!isCategorySelected(categoryId)) {
                    selectedCategoriesList.push({ id: categoryId, name: categoryName });
                }
            } else {
                selectedCategoriesList = selectedCategoriesList.filter(function(cat) {
                    return cat.id !== categoryId;
                });
            }
            updateSelectedCategoriesDisplay();
            updateHiddenCategoryFields(); // 关键修复：更新隐藏字段
        }

        function updateSelectedCategoriesDisplay() {
            if (!elements.selectedCategories) return;

            elements.selectedCategories.innerHTML = '';

            if (selectedCategoriesList.length === 0) {
                elements.selectedCategories.innerHTML = '<div class="no-categories">暂未选择分类</div>';
                return;
            }

            selectedCategoriesList.forEach(function(category) {
                var categoryTag = document.createElement('div');
                categoryTag.className = 'category-tag';
                categoryTag.innerHTML =
                    category.name +
                    '<span class="remove-category" data-id="' + category.id + '">&times;</span>';

                elements.selectedCategories.appendChild(categoryTag);
            });

            bindRemoveCategoryEvents();
        }
// ==================== 关键修复：更新隐藏的分类字段 ====================
        function updateHiddenCategoryFields() {
            var container = document.getElementById('categoryIdsContainer');
            if (!container) return;

            // 清空现有隐藏字段
            container.innerHTML = '';

            // 为每个选中的分类创建隐藏输入字段
            selectedCategoriesList.forEach(function(category) {
                var input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'categoryIds';
                input.value = category.id;
                container.appendChild(input);
            });

            console.log('更新隐藏字段，选中的分类ID:', selectedCategoriesList.map(function(cat) { return cat.id; }));

            // 如果没有选择分类，创建一个空的隐藏字段（可选）
            if (selectedCategoriesList.length === 0) {
                var emptyInput = document.createElement('input');
                emptyInput.type = 'hidden';
                emptyInput.name = 'categoryIds';
                emptyInput.value = '';
                container.appendChild(emptyInput);
            }
        }
        function bindRemoveCategoryEvents() {
            var removeButtons = elements.selectedCategories.querySelectorAll('.remove-category');
            removeButtons.forEach(function(button) {
                button.addEventListener('click', function(e) {
                    e.stopPropagation();
                    var categoryId = parseInt(this.getAttribute('data-id'));
                    selectedCategoriesList = selectedCategoriesList.filter(function(cat) {
                        return cat.id !== categoryId;
                    });
                    updateSelectedCategoriesDisplay();
                    updateHiddenCategoryFields(); // 关键修复：更新隐藏字段
                    var checkbox = elements.modalCategories.querySelector('input[value="' + categoryId + '"]');
                    if (checkbox) {
                        checkbox.checked = false;
                        checkbox.closest('.category-item').classList.remove('selected');
                    }
                });
            });
        }

        if (elements.confirmCategorySelectionBtn) {
            elements.confirmCategorySelectionBtn.addEventListener('click', closeModal);
        }

        // ==================== 字数统计功能 ====================
        if (elements.summaryInput && elements.summaryCount) {
            elements.summaryInput.addEventListener('input', function() {
                var count = this.value.length;
                elements.summaryCount.textContent = count + '/200';
                elements.summaryCount.style.color = count > 200 ? '#e74c3c' : '#777';
            });
            // 初始化字数
            elements.summaryInput.dispatchEvent(new Event('input'));
        }

        if (elements.contentInput && elements.contentCount) {
            elements.contentInput.addEventListener('input', function() {
                var count = this.value.length;
                elements.contentCount.textContent = count + ' 字';
            });
            // 初始化字数
            elements.contentInput.dispatchEvent(new Event('input'));
        }

        // ==================== 表单处理 ====================
        if (elements.articleForm) {
            elements.articleForm.addEventListener('submit', function(e) {
                console.log('表单提交，准备发布文章');

                if (!validateForm()) {
                    e.preventDefault();
                    return false;
                }

                // 确保分类数据正确提交
                updateHiddenCategoryFields();

                console.log('表单验证通过，准备提交');
                return true;
            });
        }

        function validateForm() {
            var title = elements.titleInput ? elements.titleInput.value.trim() : '';
            var content = elements.contentInput ? elements.contentInput.value.trim() : '';
            var summary = elements.summaryInput ? elements.summaryInput.value.trim() : '';
            var isValid = true;

            if (!title) {
                showMessage('请输入文章标题', 'error');
                if (elements.titleInput) elements.titleInput.focus();
                isValid = false;
            } else if (!summary) {
                showMessage('请输入文章摘要', 'error');
                if (elements.summaryInput) elements.summaryInput.focus();
                isValid = false;
            } else if (!content) {
                showMessage('请输入文章内容', 'error');
                if (elements.contentInput) elements.contentInput.focus();
                isValid = false;
            }

            return isValid;
        }

        // ==================== 辅助功能 ====================
        function showMessage(text, type) {
            if (!elements.messageContainer) return;

            // 清空现有消息
            elements.messageContainer.innerHTML = '';

            var message = document.createElement('div');
            message.className = 'message ' + type;
            message.textContent = text;

            elements.messageContainer.appendChild(message);

            setTimeout(function() {
                if (message.parentNode === elements.messageContainer) {
                    message.remove();
                }
            }, 3000);
        }

        // 初始化页面
        updateSelectedCategoriesDisplay();
        updateHiddenCategoryFields(); // 关键修复：初始化隐藏字段
    });
</script>
</body>
</html>