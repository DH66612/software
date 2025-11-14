package com.echo.utils;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;


public class JdbcUtils {

    private static String url;
    private static String user;
    private static String password;
    private static String driver;

    // 静态代码块，在类加载时执行
    static {
        try {
            // 1. 读取配置文件
            InputStream is = JdbcUtils.class.getClassLoader().getResourceAsStream("db.properties");//加载数据库配置文件
            Properties props = new Properties();//创建配置属性对象
            props.load(is);//加载配置文件内容

            // 2. 获取配置信息
            url = props.getProperty("url");
            user = props.getProperty("user");
            password = props.getProperty("password");
            driver = props.getProperty("driver");//获取数据库连接配置参数

            // 3. 注册驱动
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("数据库配置初始化失败", e);
        }
    }


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }


    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }//关闭数据库结果集

        if (stmt != null) {//关闭语句对象
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void close(Statement stmt, Connection conn) {
        close(null, stmt, conn);
    }
}