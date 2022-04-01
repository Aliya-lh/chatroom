package org.example.api;

import org.example.dao.ChannelDao;
import org.example.model.Channel;
import org.example.model.User;
import org.example.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/channelList")
public class ChannelListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //返回频道列表及登录用户信息

        //校验是否登录
        //req.getSession(false)：根据请求（从Cookie: JSESSIONID=xxx;）
        // 获取当前session对象，如果没有获取到，返回null
        User loginUser = WebUtil.getLoginUser(req.getSession(false));
        if(loginUser == null){//没有登录，不允许访问
            //设置http响应状态码403（Forbidden禁止访问）
            resp.setStatus(403);
            return;
        }

        //登录成功，获取频道列表数据
        List<Channel> channels = ChannelDao.selectAll();
        //返回的响应正文json字符串结构，前端使用相同结构解析就行
        Map<String, Object> map = new HashMap<>();
        map.put("user", loginUser);
        map.put("channels", channels);

        //返回响应数据
        String body = WebUtil.write(map);
        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().write(body);
    }
}
