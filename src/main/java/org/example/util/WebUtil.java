package org.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.User;
import org.junit.Test;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class WebUtil {

    private static final ObjectMapper M = new ObjectMapper();

    //需要把消息中的日期，转换为年-月-日 时:分:秒
    static {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        M.setDateFormat(df);
    }

    //服务端保存上传头像的路径
    public static final String LOCAL_HEAD_PATH = "/root/upload";

    //序列化：提供将Java对象转换为json字符串的方法
    public static String write(Object o){
        try {
            return M.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("转换Java对象为json字符串出错", e);
        }
    }

    //反序列化：将json字符串转换为Java对象
    //提供两个重载方法：InputStream和String来转换
    public static <T> T read(InputStream is, Class<T> clazz){
        try {
            return M.readValue(is, clazz);
        } catch (IOException e) {
            throw new RuntimeException("转换输入流json字符串为Java对象出错", e);
        }
    }
    public static <T> T read(String str, Class<T> clazz){
        try {
            return M.readValue(str, clazz);
        } catch (IOException e) {
            throw new RuntimeException("转换输入流json字符串为Java对象出错", e);
        }
    }

    //获取session中保存的用户信息
    public static User getLoginUser(HttpSession session){
        if(session != null){
            //session不为空，返回session中获取的用户
            //获取时的键，需要和登录时保存的键一致
            return (User) session.getAttribute("user");
        }
        return null;
    }

    @Test
    public void writeTest(){
        User u = new User();
        u.setId(2);
        u.setUsername("张三");
        u.setPassword("123");
        u.setNickname("哈哈");
        System.out.println(write(u));
    }

    @Test
    public void readTest(){
        String s = "{\"id\":2,\"username\":\"张三\",\"password\":\"123\",\"nickname\":\"哈哈\",\"logoutTime\":null}";
        System.out.println(read(s, User.class));
    }
}
