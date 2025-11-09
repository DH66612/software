create database if not exists echo_network;
use echo_network;
create table if not exists users
(
    id       int primary key auto_increment comment '用户ID',
    username varchar(50) comment '用户名',
    password varchar(255) comment '用户密码',
    email    varchar(100) comment '用户邮箱',
    nickname varchar(255) comment '用户昵称',
    avatar  varchar(255) comment'用户头像',
    role     int default 0 comment '用户身份',
    status   int default 0 comment '用户状态',
    create_time timestamp default current_timestamp comment '创建时间',
    update_time timestamp default current_timestamp comment '更新时间'
    )comment '用户表';
create table if not exists articles (
    id int primary key  auto_increment comment '文章ID',
    title varchar(200) not null comment '文章标题',
    content text not null comment '文章内容（Markdown格式）',
    html_content text comment '文章内容（HTML格式）',
    summary text comment '文章摘要',
    author_id int not null comment '作者ID',
    status int default 1 comment '状态：0=草稿，1=已发布，2=已删除',
    view_count int default 0 comment '阅读次数',
    like_count int default 0 comment '点赞数',
    comment_count int default 0 comment '评论数',
    create_time timestamp default current_timestamp comment '创建时间',
    update_time timestamp default current_timestamp on
        update current_timestamp comment '更新时间',
    publish_time timestamp default current_timestamp comment '发布时间',
    foreign key (author_id) references users(id) on delete cascade ,
    index idx_author_id (author_id),
    index idx_status (status),
    index idx_create_time (create_time),
    index idx_publish_time (publish_time)
) comment '文章表';
create table if not exists categories (
    id int primary key auto_increment comment '分类ID',
    name varchar(50) not null comment '分类名称',
    description varchar(200) comment '分类描述',
    icon varchar(100) comment '分类图标',
    color varchar(20) comment '分类颜色',
    sort_order int default 0 comment '排序顺序',
    status int default 1 comment '状态：0=禁用，1=启用',
    create_time timestamp default current_timestamp comment '创建时间',
    update_time timestamp default current_timestamp on update current_timestamp comment '更新时间',
    unique key uk_name (name),
    index idx_status (status),
    index idx_sort_order (sort_order)
    ) engine =InnoDB
    default charset =utf8mb4
    collate=utf8mb4_unicode_ci comment ='文章分类表';
create table if not exists article_categories (
                                                  id int primary key auto_increment comment '关联ID',
                                                  article_id int not null comment '文章ID',
                                                  category_id int not null comment '分类ID',
                                                  create_time timestamp default current_timestamp comment '创建时间',
                                                  foreign key fk_article_categories_article (article_id)
    references articles(id) on update cascade ,
    foreign key fk_article_categories_category (category_id)
    references categories(id) ON DELETE cascade ,

    -- 唯一约束，防止重复关联
    unique key uk_article_category (article_id, category_id),

    -- 索引
    index idx_article_id (article_id),
    index idx_category_id (category_id)
    )
    engine =InnoDB default charset =utf8mb4
    collate =utf8mb4_unicode_ci comment ='文章分类关联表';
INSERT INTO categories (name, description, icon, color, sort_order) VALUES
('技术教程', '编程开发、技术教程类文章', '💻', '#3498db', 1),
('生活随笔', '日常生活、感悟思考类文章', '📝', '#2ecc71', 2),
('读书笔记', '读书心得、书评笔记类文章', '📚', '#e74c3c', 3),
('旅行游记', '旅行经历、风景见闻类文章', '✈️', '#f39c12', 4),
('美食分享', '美食制作、餐厅推荐类文章', '🍕', '#e67e22', 5),
('数码产品', '电子产品使用体验和评测', '📱', '#9b59b6', 6),
('影视评论', '电影、电视剧评论和推荐', '🎬', '#1abc9c', 7),
('音乐分享', '音乐推荐、乐评类文章', '🎵', '#d35400', 8);
CREATE TABLE IF NOT EXISTS comments (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
    article_id INT NOT NULL COMMENT '文章ID',
    user_id INT NOT NULL COMMENT '评论用户ID',
    content TEXT NOT NULL COMMENT '评论内容',
    status INT DEFAULT 1 COMMENT '状态：0=待审核，1=正常，2=删除',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_article_id (article_id),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) COMMENT '评论表';



