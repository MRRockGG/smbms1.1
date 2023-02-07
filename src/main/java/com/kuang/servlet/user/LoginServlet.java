package com.kuang.servlet.user;

import com.kuang.pojo.User;
import com.kuang.service.user.UserServiceImpl;
import com.kuang.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    //Servlet:控制层调用业务层代码
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LoginServlet--start。。。");

        //获取用户名密码
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");

        //和数据库中的密码进行对比，调用业务层

        UserServiceImpl userService = new UserServiceImpl();
        User user = userService.login(userCode, userPassword);//这里已经把用户查取出来放到user对象里面了
        if (user != null && userPassword.equals(user.getUserPassword())) {
            //查有此人可以登录
            //将用户的信息放到session中
            req.getSession().setAttribute(Constants.USER_SESSION, user);
            //跳转到内部主页
            resp.sendRedirect("jsp/frame.jsp");

        } else {//无法登陆
            //转发会登录页面，提示，用户名或者密码错误
            req.setAttribute("error", "用户名或者密码不正确");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
