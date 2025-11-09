package com.echo.dao;
import com.echo.entity.User;
import java.util.List;
public interface UserDao {
    User findById(Integer Id);
    User findByUsername(String username);
    User findByEmail(String email);
    int insert(User user);
    int update(User user);
    int updatePassword(User user);
    int updateStatus(User user);
    List<User> findAll(int offset,int limit);//用于从数据库里查询用户列表，offset为起始位置，limit为查询的记录条数
    int count();

    boolean updateAvatar(Integer userId, String avatarUrl);


List<User> search(String keyword, int offset, int limit);//用关键字查找

}
