package com.echo.entity;
import java.util.Date;

public class User {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String nickname;
    private Integer role=0;//用户身份
    private Integer status=0;//用户状态
    private String avatar;//头像的URL
    private Date createTime;//创建时间

    private Date updateTime;//更新事件
    public User(){//无参构造器
        this.createTime = new Date();
        this.updateTime = new Date();
    }
    public User(Integer id, String username, String password, String email, Integer role, Integer status, String avatar) {
        this();
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname =username;
        this.role = role;
        this.status = status;
        this.avatar = avatar;
        this.createTime = new Date();
        this.updateTime = new Date();
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public Integer getRole() {
        return role;
    }
    public void setRole(Integer role) {
        this.role = role;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
    public boolean isAdmin(){
        return role==1;
    }
    public boolean isDisabled(){
        return status!=null&&status==1;
    }
    public boolean isValid(){
        return !isDisabled();
    }

    @Override
    public String toString() {
        return "User{"+
                "id="+id+
                ",username='"+username+'\''+
                ",email='"+email+'\''+
                ",role='"+role+
                ",status="+status+
                '}';
    }
    @Override
    public boolean equals(Object o) {//定义两个user对象相同的条件，拥有相同的类且id字段相等
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id)&&id!=null;
    }
    @Override//哈希表用来提高对象的搜查效率，以固定的方式计算出一个对象的哈希码值，与equals方法联动起来，
    //就可以达到相同的对象有相同的哈希码值了
    public int hashCode() {
        return id!=null?id.hashCode():0;
    }



}

