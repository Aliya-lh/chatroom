package org.example.dao;

import org.example.model.User;
import org.example.util.DBUtil;
import org.junit.Test;

import java.sql.*;

public class UserDao {

    //注册：检查账号、昵称是否已存在；登录：校验账号是否存在
    public static User checkIfExist(String username, String nickname) {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            c = DBUtil.getConnection();
            String sql = "select * from user where username=?";
            if(nickname != null){
                sql += " or nickname=?";
            }
            ps = c.prepareStatement(sql);
            ps.setString(1, username);
            if(nickname != null) {
                ps.setString(2, nickname);
            }
            rs = ps.executeQuery();
            User query = null;
            while (rs.next()){
                query = new User();
                //注册servlet中，只判断了是否返回的user对象为空，可以不设置属性
                //登录中，要根据账号查询其他字段
                Integer id = rs.getInt("id");
                String loginNickname = rs.getString("nickname");
                String password = rs.getString("password");
                String head = rs.getString("head");
                java.sql.Timestamp logoutTime = rs.getTimestamp("logout_time");
                query.setId(id);
                query.setUsername(username);
                query.setPassword(password);
                query.setNickname(loginNickname);
                query.setHead(head);
                if(logoutTime != null){
                    long l = logoutTime.getTime();
                    query.setLogoutTime(new java.util.Date(l));
                }
            }
            return query;
        } catch (SQLException e) {
            throw new RuntimeException("注册检查账号昵称是否存在jdbc出错", e);
        } finally {
            DBUtil.close(c, ps, rs);
        }
    }

    public static int updateLogoutTime(User loginUser) {
        Connection c = null;
        PreparedStatement ps = null;
        try{
            c = DBUtil.getConnection();
            String sql = "update user set logout_time=? where id=?";//注意拼接字符串时，空格
            ps = c.prepareStatement(sql);
            long currentTime = loginUser.getLogoutTime().getTime();
            ps.setTimestamp(1, new Timestamp(currentTime));
            ps.setInt(2, loginUser.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("更新用户上次注销时间jdbc出错", e);
        } finally {
            DBUtil.close(c, ps);
        }
    }

    @Test
    public void checkIfExistTest(){
        //注册测试
//        User query = checkIfExist("abc", "");
        //登录测试
        User query = checkIfExist("abc", null);
        System.out.println(query);
    }

    public static int insert(User user) {
        Connection c = null;
        PreparedStatement ps = null;
        try{
            c = DBUtil.getConnection();
            String sql = "insert into user(username, password, nickname, head)" +
                    " values(?,?,?,?)";//注意拼接字符串时，空格
            ps = c.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getNickname());
            ps.setString(4, user.getHead());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("注册插入用户数据jdbc出错", e);
        } finally {
            DBUtil.close(c, ps);
        }
    }

    @Test
    public void insertTest(){
        User u = new User();
        u.setUsername("abc");
        u.setPassword("123");
        u.setNickname("刘同学");
        insert(u);
    }
}
