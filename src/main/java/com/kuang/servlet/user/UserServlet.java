package com.kuang.servlet.user;

import com.kuang.dao.BaseDao;
import com.kuang.pojo.User;
import com.kuang.service.user.UserServiceImpl;
import com.kuang.util.Constants;
import com.mysql.jdbc.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

//实现Servlet复用
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method!=null&&method.equals("updatePwd")){
            this.updatePwd(req, resp);
        }else if(method.equals("pwdModify")&&method!=null){
            this.pwdModify(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    //修改密码
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp){
        //从Session里面拿id
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");


        System.out.println("UserServlet"+newpassword);
        System.out.println(o);
        Boolean flag = false;

        if (o!=null&&!StringUtils.isNullOrEmpty(newpassword)){
            UserServiceImpl userService = new UserServiceImpl();

            flag = userService.updatePwd(((User) o).getId(), newpassword);
            System.out.println(flag);
            if(flag){
                req.setAttribute("message","修改密码成功，请退出，使用新密码登录。");
                //密码修改成功后，移除session
                req.getSession().removeAttribute(Constants.USER_SESSION);
            }else {
                req.setAttribute("message","密码修改失败。");
                //密码修改失败后，移除session
            }
        }else {
            req.setAttribute("message","新密码格式错误。");
        }
        //返回登录页面
        try {
            req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //验证旧密码，通过session中的用户密码
    public  void pwdModify(HttpServletRequest req, HttpServletResponse resp){
        //从Session里面拿id,旧密码
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");
        //万能map
        HashMap<String, String> resultMap = new HashMap<>();
        if(o==null){//Session失效，Session过期了
            resultMap.put("result","sessionerror");

        }else if(StringUtils.isNullOrEmpty(oldpassword)){//输入的密码为空
                resultMap.put("result","error");
        }else{
            String userPassword = ((User)o).getUserPassword();
            if(userPassword.equals(oldpassword)){
                resultMap.put("result","true");
            }else {
                resultMap.put("result","false");
            }

        }

    }
}


