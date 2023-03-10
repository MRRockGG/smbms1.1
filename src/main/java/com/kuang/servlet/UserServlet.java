package com.kuang.servlet;

import com.alibaba.fastjson.JSONArray;
import com.kuang.dao.BaseDao;
import com.kuang.pojo.Role;
import com.kuang.pojo.User;
import com.kuang.service.role.RoleServiceImpl;
import com.kuang.service.user.UserServiceImpl;
import com.kuang.util.Constants;
import com.kuang.util.PageSupport;
import com.mysql.jdbc.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//实现Servlet复用
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method!=null&&method.equals("updatePwd")){
            this.updatePwd(req, resp);
        }else if(method.equals("pwdModify")&&method!=null){
            this.pwdModify(req, resp);
        }else if(method.equals("query")&&method!=null){
            this.query(req, resp);
        }else if (method!=null&&method.equals("ucexist")){
            this.ucexist(req,resp);
        }else if (method!=null&&method.equals("getrolelist")){
            this.getrolelist(req,resp);
        }else if (method!=null&&method.equals("add")){
            this.add(req,resp);
        }
    }

    @Override
    //新增usercode是否存在

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    //重点，难点，用户管理分页
    public void query(HttpServletRequest req, HttpServletResponse resp){
        //查询用户列表

        //从前端获取数据
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;

        // 获取用户列表
        UserServiceImpl userService = new UserServiceImpl();
        List<User> userList =null;
        //页面设置
        int pageSize = 5;
        int currentPageNo = 1;

        if(queryUserName==null){
            queryUserName = "";
        }
        if (temp!=null && !temp.equals("")){
            queryUserRole=Integer.parseInt(temp);//给查询赋值0，1，2，3
        }
        if(pageIndex!=null){
            currentPageNo=Integer.parseInt(pageIndex);
        }
        //System.out.println("currentPageNo:"+currentPageNo+"pageIndex"+pageIndex);
        //获取用户的总数（分页：上一页，下一页）
        int totalCount = userService.getUserCount(queryUserName,queryUserRole);
        //System.out.println("totalCount:"+totalCount);//12
        //总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);

        pageSupport.setPageSize(pageSize);

        pageSupport.setTotalPageCount(totalCount);

        int totalPageCount = totalCount/pageSize+1;

        //控制首页和尾页
        //如果页面要小于1了，就显示第一页的东西


        if(currentPageNo<1){
            currentPageNo = 1;
        }else if(currentPageNo>totalPageCount){
            currentPageNo = totalPageCount;
        }


        System.out.println("查找currentPageNo的值servlet传入"+currentPageNo);//0
        System.out.println();
        //获取用户列表展示
        //currentPageNo =1;

        userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList",userList);
        //System.out.println("获取用户列表展示");
//        for (User user : userList) {
//            System.out.printf(user.getUserName());
//        }
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList=roleService.getRoleList();
        req.setAttribute("roleList",roleList);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("queryUserName",queryUserName);
        req.setAttribute("queryUserRole",queryUserRole);

        //返回前端测试

        try {
            req.getRequestDispatcher("userlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        HashMap<String, String> resultMap = new HashMap<String, String>();
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
        try{
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            //JSONArray 阿里巴巴的json工具类，转换格式
//            resultMap = ["result","sessionerror","result","error"]
//            Json格式 = {key，value}
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();

        }

    }

    public  void ucexist(HttpServletRequest req, HttpServletResponse resp){
        //从页面拿输入的userCode
        String usercode = req.getParameter("userCode");
        //查询此usercode是否存在

        //万能map
        HashMap<String, String> resultMap = new HashMap<String, String>();


        //默认存在
        Boolean flag = false;
        //如果不存在，纳闷flag就是fluse


        System.out.println("StringUtils.isNullOrEmpty(usercode):"+StringUtils.isNullOrEmpty(usercode));
        System.out.println("usercode:"+usercode);
        if(!StringUtils.isNullOrEmpty(usercode)) {
            UserServiceImpl userService = new UserServiceImpl();
            flag = userService.ucIsExist(usercode);
            System.out.println(flag);
            if (flag) {//如果flag为真那么说明存在此usercode
                resultMap.put("userCode", "exist");
            }//否则的话flag为假说明不存在
        }else {
            resultMap.put("userCode", "null");
        }

        try{
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            //JSONArray 阿里巴巴的json工具类，转换格式
//            resultMap = ["result","sessionerror","result","error"]
//            Json格式 = {key，value}
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();

        }


    }

    public void getrolelist(HttpServletRequest req, HttpServletResponse resp){

//        HashMap<String, String> resultMap = new HashMap<String, String>();//存放ajaxdata数据
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList=roleService.getRoleList();//role_id,role,code,role_name

//        for (Role role : roleList) {
//            resultMap.put(role.getId().toString(),role.getRoleName());
//        }

        try{
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            //JSONArray 阿里巴巴的json工具类，转换格式
//            resultMap = ["result","sessionerror","result","error"]
//            Json格式 = {key，value}
            writer.write(JSONArray.toJSONString(roleList));
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();

        }

    }

    public void add(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());

        UserServiceImpl userService = new UserServiceImpl();
        boolean flag = userService.add(user);
        System.out.println("add成功吗"+flag);
        if (flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else {
            req.getRequestDispatcher("useradd.jsp").forward(req,resp);
        }

    }
}


