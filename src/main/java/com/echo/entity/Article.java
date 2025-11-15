package com.echo.entity;
import com.echo.entity.Category;

import  java.util.Date;
import  java.util.List;

public class Article{
    private Integer id;
    private String title;
    private String content;
    private String authorAvatar;
    private Integer author_id;
    private String html_content;
    private Integer status=1;
    private Date create_time;
    private Date update_time;
    private Date publish_time;
    private Integer view_count;
    private Integer like_count;
    private Integer comment_count;
    private List<Category> categories;
    private List<Integer> category_ids;
    private String summary;
    private String authorName;

    public Article(){
        this.create_time =new Date();
        this.publish_time =new Date();
        this.update_time =new Date();
    }
    public Article(String title,String content,Integer authorid){
        this();
        this.title=title;
        this.content=content;
        this.author_id=authorid;

    }

    public Integer getid() {
        return id;
    }
    public void setid(Integer id) {
        this.id = id;
    }
    public String gettitle() {
        return title;
    }
    public void settitle(String title) {
        this.title = title;

    }
    public String getcontent() {
        return content;
    }
    public void setcontent(String content) {
        this.content = content;
    }
    public Integer getauthorid() {
        return author_id;
    }
    public void setauthorid(Integer authorid) {
        this.author_id = authorid;
    }
    public String getHtmlContent() {
        return html_content;
    }
    public void setHtmlContent(String htmlcontent) {
        this.html_content = htmlcontent;
    }

    public Integer getstatus() {
        return status;
    }
    public void setstatus(Integer status) {
        this.status = status;
    }
    public Date getcreateTime() {
        return create_time;
    }
    public void setcreateTime(Date createTime) {
        this.create_time = createTime;
    }
    public Date getupdateTime() {
        return update_time;
    }
    public void setupdateTime(Date updateTime) {
        this.update_time = updateTime;
    }
    public Date getpublishTime() {
        return publish_time;
    }
    public void setpublishTime(Date publishTime) {
        this.publish_time = publishTime;
    }
    public Integer getviewCount() {
        return view_count;
    }
    public void setviewCount(Integer viewCount) {
        this.view_count = viewCount;
    }
    public Integer getlikeCount() {
        return like_count;
    }
    public void setlikeCount(Integer likeCount) {
        this.like_count = likeCount;
    }
    public Integer getcommentCount() {
        return comment_count;
    }
    public void setcommentCount(Integer commentCount) {
        this.comment_count = commentCount;
    }
    public List<Category> getcategories() {
        return categories;
    }
    public void setcategories(List<Category> categories) {
        this.categories = categories;
    }
    public List<Integer> getcategoryids() {
        return category_ids;
    }
    public void setcategoryids(List<Integer> categoryids) {
        this.category_ids = categoryids;
    }
    public boolean isPublished(){
        return status == 1&&status != null;
    }
    public boolean isDrafted(){
        return status == 0&&status != null;
    }
    public boolean isDeleted(){
        return status == 2&&status != null;
    }
    public void increaseviewCount(){
        if(view_count ==null)
            view_count =0;
        this.view_count++;
    }
    public void increaselikeCount(){
        if(like_count ==null)
            like_count =0;
        this.like_count++;
    }
    public void increasecommentCount(){
        if(comment_count ==null)
            comment_count =0;
        this.comment_count++;
    }
    public void decreasecommentCount(){
        if(comment_count ==null)
            comment_count =0;
        this.comment_count--;
    }//检查文章是否属于指定用户
    public boolean isOwnedBy(User user){
        return author_id.equals(user.getId());
    }
    public boolean isOwnedBy(Integer userid){
        return author_id.equals(userid);
    }
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author_id=" + author_id +
                ", status=" + status +
                ", view_count=" + view_count +
                ", like_count=" + like_count +
                ", comment_count=" + comment_count +
                ", categories=" + categories +
                ", summary='" + summary + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;return id != null && id.equals(article.id);
        //定义两个Category对象相同的条件，拥有相同的类且id字段相等
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    public String getauthorAvatar() {
        return authorAvatar;
    }
    public void setauthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorName() {
        return authorName;
    }
}//哈希表用来提高对象的搜查效率，以固定的方式计算出一个对象的哈希码值，与equals方法联动起来，
//就可以达到相同的对象有相同的哈希码值了





