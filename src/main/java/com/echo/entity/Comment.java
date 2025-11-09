package com.echo.entity;



import java.util.Date;

public class Comment {
    private Integer id;
    private Integer articleId;
    private Integer userId;
    private String content;
    private Integer status; // 0=待审核，1=正常，2=删除
    private Date createTime;
    private Date updateTime;

    // 非数据库字段，用于显示
    private String authorName;
    private String authorAvatar;

    // 构造方法
    public Comment() {}

    public Comment(Integer articleId, Integer userId, String content) {
        this.articleId = articleId;
        this.userId = userId;
        this.content = content;
        this.status = 1; // 默认正常状态
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    // getter 和 setter 方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getArticleId() { return articleId; }
    public void setArticleId(Integer articleId) { this.articleId = articleId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getAuthorAvatar() { return authorAvatar; }
    public void setAuthorAvatar(String authorAvatar) { this.authorAvatar = authorAvatar; }
}