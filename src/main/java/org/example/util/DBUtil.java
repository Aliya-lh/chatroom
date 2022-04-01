package org.example.util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    //定义一个单例的数据源/连接池对象
    //TODO: 可以改造为双重校验锁的单例模式（目前是饿汉式的实现）
    private static final MysqlDataSource DS = new MysqlDataSource();

    static {
        DS.setURL("jdbc:mysql://127.0.0.1:3306/chatroom");
        DS.setUser("root");
        DS.setPassword("Lh120108");
        DS.setUseSSL(false);
        DS.setUseUnicode(true);
        DS.setCharacterEncoding("UTF-8");
    }

    //获取数据库连接
    public static Connection getConnection(){
        try {
            return DS.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("获取数据库连接失败", e);
        }
    }

    public static void close(Connection c, Statement s){
        close(c, s, null);
    }

    //释放jdbc操作数据库资源
    public static void close(Connection c, Statement s, ResultSet rs){
        try {
            if(rs != null) rs.close();
            if(s != null) s.close();
            if(c != null) c.close();
        } catch (SQLException e) {
            throw new RuntimeException("jdbc释放数据库资源出错", e);
        }
    }

    @Test
    public void getConnectionTest(){
        System.out.println(getConnection());
    }
}
