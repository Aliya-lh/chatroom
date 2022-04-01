package org.example.api;

import org.example.dao.UserDao;
import org.example.model.User;
import org.example.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = WebUtil.getLoginUser(session);
        if(user == null){//没有登录，直接返回403状态码
            resp.setStatus(403);
            return;
        }
        session.removeAttribute("user");//删除session中保存的用户
        //记录用户上次注销时间
        user.setLogoutTime(new java.util.Date());
        UserDao.updateLogoutTime(user);
        resp.sendRedirect("index.html");
    }
}
