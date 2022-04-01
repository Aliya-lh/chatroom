package org.example.dao;

import org.example.model.Message;
import org.example.util.DBUtil;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageDao {
    //查询用户上次注销后的历史消息
    public static List<Message> query(Date logoutTime) {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = DBUtil.getConnection();
            String sql = "select * from message";
            //刚注册的用户，没有上次注销时间，要判断一下
            if(logoutTime != null){
                sql += " where send_time > ?";
            }
            ps = c.prepareStatement(sql);
            if(logoutTime != null) {
                ps.setTimestamp(1, new Timestamp(logoutTime.getTime()));
            }
            rs = ps.executeQuery();
            List<Message> messages = new ArrayList<>();
            while (rs.next()){
                Message m = new Message();
                Integer id = rs.getInt("id");
                String content = rs.getString("content");
                Integer userId = rs.getInt("user_id");
                String userNickname = rs.getString("user_nickname");
                Integer channelId = rs.getInt("channel_id");
                Timestamp sendTime = rs.getTimestamp("send_time");

                //设置到message对象中
                m.setId(id);
                m.setContent(content);
                m.setUserId(userId);
                m.setUserNickname(userNickname);
                m.setChannelId(channelId);
                m.setSendTime(new Date(sendTime.getTime()));

                messages.add(m);
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException("查询历史消息jdbc出错", e);
        } finally {
            DBUtil.close(c, ps, rs);
        }
    }

    public static int insert(Message m) {
        Connection c = null;
        PreparedStatement ps = null;
        try{
            c = DBUtil.getConnection();
            //数据库mysql, now()是一个日期函数，返回当前日期
            String sql = "insert into message(content, user_id, user_nickname" +
                    ", channel_id, send_time) values (?,?,?,?, now())";//注意拼接字符串时，空格
            ps = c.prepareStatement(sql);
            ps.setString(1, m.getContent());
            ps.setInt(2, m.getUserId());
            ps.setString(3, m.getUserNickname());
            ps.setInt(4, m.getChannelId());
            m.setSendTime(new Date());//这个message对象，还需要返回给前端展示
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("保存发送的消息jdbc出错", e);
        } finally {
            DBUtil.close(c, ps);
        }
    }

    @Test
    public void insertTest(){
        Message m = new Message();
        m.setUserId(1);
        m.setContent("测试一下");
        m.setUserNickname("哈哈");
        m.setChannelId(2);
        insert(m);
    }
}
