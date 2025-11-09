package com.echo.service;
import com.echo.entity.User;
public interface UserService {
    User register(User user);
    User login(String username, String password);

    User getUserById(Integer userId);
    boolean updateAvatar(Integer userId, String avatarUrl);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    User updateProfile(User user)throws RuntimeException;
    boolean updatePassword(Integer userId, String oldPassword, String newPassword);
    boolean resetPassword(String email, String newPassword)throws RuntimeException;
    boolean isUsernameExist(String username)throws RuntimeException;
    boolean isEmailExist(String email)throws RuntimeException;
    boolean disableUser(Integer userId)throws RuntimeException;
    boolean enableUser(Integer userid)throws RuntimeException;
    java.util.List<User> getAllUser(int page,int pagesize);
    int getAllUserCount()throws RuntimeException;
    java.util.List<User>searchUser(String keyword,int page,int pagesize);
}
