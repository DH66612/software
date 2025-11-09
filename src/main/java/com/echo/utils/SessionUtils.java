//会话工具类，用来判断当前登录用户
package com.echo.utils;
import com.echo.entity.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
public class SessionUtils {
  public static User getCurrentUser(HttpServletRequest request) {
      HttpSession session = request.getSession(false);//获取用户的session，如果不存在就返回null
      if (session== null) {
          return null;
      }
      User user=(User)session.getAttribute("currentUser");
      return user;
  }
  public static void setCurrentUser(HttpServletRequest request,User user) {
	HttpSession session = request.getSession();//获取或创建session，用来把当前登录用户设置在session
    session.setAttribute("currentUser",user);
  }
  public static boolean isLoggedIn(HttpServletRequest request) {
      return getCurrentUser(request) != null;//这个函数的返回值如果是true则说明用户已登录，反之则为未登录

  }
















}
