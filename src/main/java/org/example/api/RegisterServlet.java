package org.example.api;

import org.example.dao.UserDao;
import org.example.model.JsonResult;
import org.example.model.User;
import org.example.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/register")
@MultipartConfig//form-data上传文件时需要
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置请求正文的编码
        req.setCharacterEncoding("UTF-8");
        //获取form-data格式的数据，有上传文件，记得加Servlet注解@MultipartConfig
        String username = req.getParameter("username");//页面必填，非空
        String password = req.getParameter("password");//页面必填，非空
        String nickname = req.getParameter("nickname");//页面必填，非空
        Part headFile = req.getPart("headFile");//页面选填，可能为空

        //注册需要把数据插入数据库，先把数据设置到User对象属性中
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);

        //保存上传的头像在服务端本地的一个路径（不要在项目的路径中，因为可能项目需要打包部署到tomcat运行）
        if(headFile != null){
            //使用一个随机字符串作为保存在服务端本地的文件名，但文件的后缀需要和上传文件的后缀一样
            //先获取上传文件名称后缀
            String fileName = headFile.getSubmittedFileName();//上传文件名
            //String.lastIndexOf(str)返回字符串最后一个匹配到str的索引位置
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            //UUID是一个随机字符串（和机器，时间戳相关）
            fileName = UUID.randomUUID() + suffix;//这个文件名需要保存在数据库
            //保存文件
            headFile.write(WebUtil.LOCAL_HEAD_PATH+"/"+fileName);
            //数据库保存头像路径：/+fileName
            user.setHead("/"+fileName);
        }

        //保存user数据到数据库：需要考虑是否账号昵称已存在，不允许插入
        //先校验账号昵称是否已存在（username = ? or nickname=?）返回一条/没有数据
        User exist = UserDao.checkIfExist(username, nickname);

        //如果存在，响应返回错误信息，如果不存在，注册（插入数据）
        //构造响应对象
        JsonResult result = new JsonResult();
        if(exist != null){
//            result.setOk(false);//ok字段是boolean，new就是false
            result.setReason("账号或昵称已存在");//真实业务报错应该更精准
        }else{
            int n = UserDao.insert(user);
            result.setOk(true);
        }

        //返回http响应给前端
        resp.setContentType("application/json; charset=utf-8");
        String body = WebUtil.write(result);
        resp.getWriter().write(body);
    }
}
