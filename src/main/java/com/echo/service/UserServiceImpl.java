
package com.echo.service;

import com.echo.dao.UserDao;
import com.echo.entity.User;
import org.mindrot.jbcrypt.BCrypt;
import com.echo.dao.UserDaoImpl;
import java.util.Date;
import java.util.List;

    public class UserServiceImpl implements UserService{

        private UserDao userDao;

        // 通过构造器注入UserDao
        public UserServiceImpl() {
            // 这里可以改为Spring注入
            this.userDao = new UserDaoImpl(); // 假设有UserDaoImpl
        }

        // 也可以通过setter注入
        public void setUserDao(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        public User register(User user) throws RuntimeException {
            System.out.println("开始用户注册: " + user.getUsername());

            // 1. 数据验证
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                throw new RuntimeException("用户名不能为空");
            }

            if (user.getPassword() == null || user.getPassword().length() < 6) {
                throw new RuntimeException("密码长度不能少于6位");
            }

            if (user.getEmail() == null || !user.getEmail().contains("@")) {
                throw new RuntimeException("邮箱格式不正确");
            }

            // 2. 检查用户名是否已存在
            if (isUsernameExist(user.getUsername())) {
                throw new RuntimeException("用户名已存在");
            }

            // 3. 检查邮箱是否已存在
            if (isEmailExist(user.getEmail())) {
                throw new RuntimeException("邮箱已被注册");
            }

            // 4. 密码加密
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashedPassword);

            // 5. 设置默认值
            if (user.getNickname() == null) {
                user.setNickname(user.getUsername()); // 默认昵称为用户名
            }
            user.setRole(0); // 默认普通用户
            user.setStatus(0); // 默认正常状态
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            // 6. 保存用户
            try {
                int result = userDao.insert(user);
                if (result > 0) {
                    System.out.println("用户注册成功: " + user.getUsername());
                    // 清除密码，不返回给上层
                    user.setPassword(null);
                    return user;
                } else {
                    throw new RuntimeException("用户注册失败");
                }
            } catch (Exception e) {
                throw new RuntimeException("用户注册失败: " + e.getMessage());
            }
        }


        @Override
        public User login(String username, String password) throws RuntimeException {
            System.out.println("=== 开始登录过程 ===");
            System.out.println("登录用户名: " + username);

            // 1. 参数验证
            if (username == null || username.trim().isEmpty()) {
                System.out.println("用户名为空");
                throw new RuntimeException("用户名不能为空");
            }

            if (password == null || password.isEmpty()) {
                System.out.println("密码为空");
                throw new RuntimeException("密码不能为空");
            }

            // 2. 根据用户名查找用户
            System.out.println("开始查询用户: " + username);
            User user = userDao.findByUsername(username);
            System.out.println("查询结果: " + (user != null ? "找到用户" : "用户不存在"));

            if (user == null) {
                System.out.println("用户不存在: " + username);
                throw new RuntimeException("用户名或密码错误");
            }

            // 3. 检查用户状态
            System.out.println("用户状态: " + user.getStatus());
            if (user.isDisabled()) {
                System.out.println("用户被禁用");
                throw new RuntimeException("用户已被禁用，请联系管理员");
            }

            // 4. 验证密码
            System.out.println("开始密码验证");
            System.out.println("输入密码: " + password);
            System.out.println("存储的加密密码: " + user.getPassword());

            if (!BCrypt.checkpw(password, user.getPassword())) {
                System.out.println("密码验证失败");
                throw new RuntimeException("用户名或密码错误");
            }

            System.out.println("密码验证成功");

            // 5. 更新最后登录时间
            user.setUpdateTime(new Date());
            userDao.update(user);

            // 6. 清除密码
            user.setPassword(null);
            System.out.println("=== 登录成功 ===");

            return user;
        }
        @Override
        public User getUserById(Integer userId) {
            if (userId == null) {
                return null;
            }

            User user = userDao.findById(userId);
            if (user != null) {
                user.setPassword(null); // 清除密码
            }
            return user;
        }

        @Override
        public User getUserByUsername(String username) {
            if (username == null || username.trim().isEmpty()) {
                return null;
            }

            User user = userDao.findByUsername(username);
            if (user != null) {
                user.setPassword(null); // 清除密码
            }
            return user;
        }

        @Override
        public User getUserByEmail(String email) {
            if (email == null || email.trim().isEmpty()) {
                return null;
            }

            User user = userDao.findByEmail(email);
            if (user != null) {
                user.setPassword(null); // 清除密码
            }
            return user;
        }

        @Override
        public User updateProfile(User user) throws RuntimeException {
            if (user == null || user.getId() == null) {
                throw new RuntimeException("用户信息不完整");
            }

            // 1. 检查用户是否存在
            User existingUser = userDao.findById(user.getId());
            if (existingUser == null) {
                throw new RuntimeException("用户不存在");
            }

            // 2. 数据验证
            if (user.getEmail() != null && !user.getEmail().contains("@")) {
                throw new RuntimeException("邮箱格式不正确");
            }

            // 3. 检查邮箱是否被其他用户使用
            if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
                User emailUser = userDao.findByEmail(user.getEmail());
                if (emailUser != null && !emailUser.getId().equals(user.getId())) {
                    throw new RuntimeException("邮箱已被其他用户使用");
                }
            }

            // 4. 更新信息（不更新密码和角色）
            existingUser.setNickname(user.getNickname());
            existingUser.setEmail(user.getEmail());
            existingUser.setAvatar(user.getAvatar());
            existingUser.setUpdateTime(new Date());

            try {
                int result = userDao.update(existingUser);
                if (result > 0) {
                    System.out.println("用户资料更新成功: " + existingUser.getUsername());
                    existingUser.setPassword(null); // 清除密码
                    return existingUser;
                } else {
                    throw new RuntimeException("用户资料更新失败");
                }
            } catch (Exception e) {
                throw new RuntimeException("用户资料更新失败: " + e.getMessage());
            }
        }

        @Override
        public boolean updatePassword(Integer userId, String oldPassword, String newPassword) throws RuntimeException {
            if (userId == null) {
                throw new RuntimeException("用户ID不能为空");
            }

            if (newPassword == null || newPassword.length() < 6) {
                throw new RuntimeException("新密码长度不能少于6位");
            }

            // 1. 获取用户信息
            User user = userDao.findById(userId);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }

            // 2. 验证旧密码
            if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
                throw new RuntimeException("旧密码错误");
            }

            // 3. 加密新密码
            String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            user.setPassword(hashedNewPassword);
            user.setUpdateTime(new Date());

            // 4. 更新密码
            try {
                int result = userDao.updatePassword(user);
                return result > 0;
            } catch (Exception e) {
                throw new RuntimeException("密码更新失败: " + e.getMessage());
            }
        }

        @Override
        public boolean resetPassword(String email, String newPassword) throws RuntimeException {
            if (email == null || email.trim().isEmpty()) {
                throw new RuntimeException("邮箱不能为空");
            }

            if (newPassword == null || newPassword.length() < 6) {
                throw new RuntimeException("新密码长度不能少于6位");
            }

            // 1. 根据邮箱查找用户
            User user = userDao.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("邮箱对应的用户不存在");
            }

            // 2. 加密新密码
            String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            user.setPassword(hashedNewPassword);
            user.setUpdateTime(new Date());

            // 3. 更新密码
            try {
                int result = userDao.updatePassword(user);
                return result > 0;
            } catch (Exception e) {
                throw new RuntimeException("密码重置失败: " + e.getMessage());
            }
        }

        @Override
        public boolean isUsernameExist(String username) {
            if (username == null || username.trim().isEmpty()) {
                return false;
            }

            User user = userDao.findByUsername(username);
            return user != null;
        }

        @Override
        public boolean isEmailExist(String email) {
            if (email == null || email.trim().isEmpty()) {
                return false;
            }

            User user = userDao.findByEmail(email);
            return user != null;
        }

        @Override
        public boolean updateAvatar(Integer userId, String avatarUrl) {
            return userDao.updateAvatar(userId, avatarUrl);
        }
        @Override
        public boolean disableUser(Integer userId) throws RuntimeException {
            if (userId == null) {
                throw new RuntimeException("用户ID不能为空");
            }

            User user = userDao.findById(userId);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }

            if (user.isDisabled()) {
                throw new RuntimeException("用户已被禁用");
            }

            user.setStatus(1); // 设置为禁用状态
            user.setUpdateTime(new Date());

            try {
                int result = userDao.updateStatus(user);
                return result > 0;
            } catch (Exception e) {
                throw new RuntimeException("禁用用户失败: " + e.getMessage());
            }
        }

        @Override
        public boolean enableUser(Integer userId) throws RuntimeException {
            if (userId == null) {
                throw new RuntimeException("用户ID不能为空");
            }

            User user = userDao.findById(userId);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }

            if (user.isValid()) {
                throw new RuntimeException("用户已是正常状态");
            }

            user.setStatus(0); // 设置为正常状态
            user.setUpdateTime(new Date());

            try {
                int result = userDao.updateStatus(user);
                return result > 0;
            }catch (Exception e){
                throw  new RuntimeException("启用用户失败："+e.getMessage());
            }
        }
        @Override
        public List<User> getAllUser(int page, int pageSize) {
            if (page < 1) page = 1;
            if (pageSize < 1) pageSize = 10;

            int offset = (page - 1) * pageSize;
            List<User> users = userDao.findAll(offset, pageSize);

            // 清除所有用户的密码
            for (User user : users) {
                user.setPassword(null);
            }

            return users;
        }

        @Override
        public int getAllUserCount() {
            return userDao.count();
        }

        @Override
        public List<User> searchUser(String keyword, int page, int pageSize) {
            if (keyword == null || keyword.trim().isEmpty()) {
                return getAllUser(page, pageSize);
            }

            if (page < 1) page = 1;
            if (pageSize < 1) pageSize = 10;

            int offset = (page - 1) * pageSize;
            List<User> users = userDao.search(keyword, offset, pageSize);

            // 清除所有用户的密码
            for (User user : users) {
                user.setPassword(null);
            }

            return users;
        }
    }