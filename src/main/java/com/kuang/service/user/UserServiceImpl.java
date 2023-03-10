package com.kuang.service.user;

import com.kuang.dao.BaseDao;
import com.kuang.dao.user.UserDao;
import com.kuang.dao.user.UserDaoImpl;
import com.kuang.pojo.User;
import com.kuang.util.Constants;
import org.junit.jupiter.api.Test;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {
    //引入dao层
    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    @Override
    public User login(String userCode, String password) {
        Connection connection = null;
        User user = new User();

        try {
            connection = BaseDao.getConnection();
            user = userDao.getLoginUser(connection, userCode);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }

        return user;//

    }

    @Override
    public Boolean updatePwd(int id, String pwd) {
        System.out.println("UserServiceImpl"+pwd);
        Connection connection = null;
        Boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if(userDao.updatePwd(connection,id,pwd)>0){

               flag = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

//    @Test
//    public void test() {
//        UserServiceImpl userServiceImpl = new UserServiceImpl();
//        User admin = userServiceImpl.login("admin", "111111");
//
//        System.out.println(admin.getUserPassword());
//
//
//    }

    @Override
    public int getUserCount(String username, int userRole) {
        System.out.println("UserServiceImpl->getUserCount");
        Connection connection = null;
        int userCount = 0;
        try {
            connection = BaseDao.getConnection();
            userCount = userDao.getUserCount(connection, username, userRole);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return userCount;
    }

    @Override
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        System.out.println("UserServiceImpl->getUserList");
        Connection connection = null;
        List<User> userList = null;
        System.out.println("queryUserName--->"+queryUserName);
        System.out.println("queryUserRole--->"+queryUserRole);
        System.out.println("currentPageNo--->"+currentPageNo);
        System.out.println("pageSize--->"+pageSize);

        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection,queryUserName,queryUserRole,currentPageNo,pageSize);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return userList;
    }

    @Override
    public Boolean ucIsExist(String userCode) {

        Connection connection = null;
        int userCodeCount = 0;
        try {
            connection = BaseDao.getConnection();
            userCodeCount = userDao.getUserCodeCount(connection, userCode);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }

        if (userCodeCount>0){
            return true;
        }else return false;



    }

    @Override
    public boolean add(User user)  {
        boolean flag = false;
        Connection connection = null;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            int upDateRows=userDao.add(connection,user);
            connection.commit();
            if (upDateRows>0){
                flag = true;
                System.out.println("add success");
            }else {
                System.out.println("add failed");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("rockback***********************");
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }finally {
            //zaiservice层进行connrction连接关闭
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }
    //有问题，没输出每个参数。
    @Test
        public void test() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        int userCount = userServiceImpl.getUserCount(null, 0);

        System.out.println(userCount);

        List<User> admin = userServiceImpl.getUserList("", 2, 1, 5);
        for (User user : admin) {
            System.out.printf("ceshi"+user.getUserName());
        }

    }
}
