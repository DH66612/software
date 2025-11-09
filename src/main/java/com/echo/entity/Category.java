package com.echo.entity;

import java.util.Date;

/**
 * 分类实体类
 */
public class Category {
    private Integer id;
    private String name;
    private String description;
    private String icon;
    private String color;
    private Integer sortOrder;
    private Integer status;
    private Date createTime;
    private Date updateTime;

    // 文章数量（非数据库字段，用于统计）
    private Integer articleCount;

    // 无参构造器
    public Category() {}

    // 带参数构造器
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = 1;
        this.sortOrder = 0;
    }

    // Getter 和 Setter 方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }

    // 业务方法
    public boolean isEnabled() {
        return status != null && status == 1;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {//定义两个Category对象相同的条件，拥有相同的类且id字段相等
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id != null && id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }//哈希表用来提高对象的搜查效率，以固定的方式计算出一个对象的哈希码值，与equals方法联动起来，
    //就可以达到相同的对象有相同的哈希码值了
}
