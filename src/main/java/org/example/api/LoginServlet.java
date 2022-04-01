package org.example.api;

import org.example.dao.UserDao;
import org.example.model.JsonResult;
import org.example.model.User;
import org.example.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        //解析json数据，使用输入流
        User input = WebUtil.read(req.getInputStream(), User.class);
        //校验账号密码
        //可以先校验账号是否存在（username=?）如果不存在，提示账号不存在，如果存在，再校验密码
        User exist = UserDao.checkIfExist(input.getUsername(), null);

        //准备返回的数据
        JsonResult result = new JsonResult();
        if(exist == null){
            result.setReason("账号不存在");
        }else{
            //校验用户输入的密码和该账号在数据库的密码是否一致
            if(!exist.getPassword().equals(input.getPassword())){//不一致
                result.setReason("账号或密码错误");
            }else{
                //校验成功：创建session并保存用户信息
                HttpSession session = req.getSession();//=getSession(true)
                session.setAttribute("user", exist);//保存数据库查询的用户
                result.setOk(true);
            }
        }

        //返回响应数据
        resp.setContentType("application/json; charset=utf-8");
        String body = WebUtil.write(result);
        resp.getWriter().write(body);
    }
}
